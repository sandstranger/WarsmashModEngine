package net.warsmash.parsers.jass.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.etheller.interpreter.ast.execution.JassThread;
import com.etheller.interpreter.ast.function.JassFunction;
import com.etheller.interpreter.ast.function.JassNativeManager;
import com.etheller.interpreter.ast.scope.GlobalScope;
import com.etheller.interpreter.ast.scope.TriggerExecutionScope;
import com.etheller.interpreter.ast.value.CodeJassValue;
import com.etheller.interpreter.ast.value.JassValue;
import com.etheller.interpreter.ast.value.StringJassValue;
import com.etheller.interpreter.ast.value.visitor.CodeJassValueVisitor;
import com.etheller.interpreter.ast.value.visitor.IntegerJassValueVisitor;
import com.etheller.interpreter.ast.value.visitor.RealJassValueVisitor;
import com.etheller.interpreter.ast.value.visitor.StringJassValueVisitor;

import net.warsmash.parsers.jass.SmashJassParser;

public class SmashJassRunner {
	public static final boolean REPORT_SYNTAX_ERRORS = true;

	static class SleepingData {
		JassThread thread;
		long wakeTime;

		public SleepingData(final JassThread thread, final long wakeTime) {
			this.thread = thread;
			this.wakeTime = wakeTime;
		}
	}

	public static void main(final String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <JassFiles> [<AdditionaFile>...]");
			return;
		}
		final long start = System.currentTimeMillis();
		final GlobalScope globals = new GlobalScope();
		final JassNativeManager jassNativeManager = new JassNativeManager();
		jassNativeManager.createNative("BJDebugMsg", new JassFunction() {
			@Override
			public JassValue call(final List<JassValue> arguments, final GlobalScope globalScope,
					final TriggerExecutionScope triggerScope) {
				for (final JassValue argument : arguments) {
					System.out.println(argument.visit(StringJassValueVisitor.getInstance()));
				}
				return null;
			}
		});
		jassNativeManager.createNative("PrintString", new JassFunction() {
			@Override
			public JassValue call(final List<JassValue> arguments, final GlobalScope globalScope,
					final TriggerExecutionScope triggerScope) {
				for (final JassValue argument : arguments) {
					System.out.println(argument.visit(StringJassValueVisitor.getInstance()));
				}
				return null;
			}
		});
		jassNativeManager.createNative("I2S", new JassFunction() {
			@Override
			public JassValue call(final List<JassValue> arguments, final GlobalScope globalScope,
					final TriggerExecutionScope triggerScope) {
				final Integer x = arguments.get(0).visit(IntegerJassValueVisitor.getInstance());
				return new StringJassValue(x.toString());
			}
		});
		jassNativeManager.createNative("StartThread", new JassFunction() {
			@Override
			public JassValue call(final List<JassValue> arguments, final GlobalScope globalScope,
					final TriggerExecutionScope triggerScope) {
				final CodeJassValue threadFunction = arguments.get(0).visit(CodeJassValueVisitor.getInstance());
				globalScope.queueThread(globalScope.createThread(threadFunction));
				return null;
			}
		});
		final List<SleepingData> sleepingThreadData = new ArrayList<>();
		jassNativeManager.createNative("Sleep", new JassFunction() {
			@Override
			public JassValue call(final List<JassValue> arguments, final GlobalScope globalScope,
					final TriggerExecutionScope triggerScope) {
				final Double sleepTime = arguments.get(0).visit(RealJassValueVisitor.getInstance());
				final JassThread currentThread = globalScope.getCurrentThread();
				currentThread.setSleeping(true);
				sleepingThreadData
						.add(new SleepingData(currentThread, System.currentTimeMillis() + (long) (sleepTime * 1000.0)));
				return null;
			}
		});
		for (final String arg : args) {
			try {
				try (FileReader reader = new FileReader(arg)) {
					final SmashJassParser smashJassParser = new SmashJassParser(reader);
					smashJassParser.scanAndParse(arg, globals, jassNativeManager);
				}
			}
			catch (final Exception e) {
				e.printStackTrace();
			}
		}
		globals.runThreadUntilCompletion(
				globals.createThread(globals.getUserFunctionInstructionPtr(GlobalScope.INIT_GLOBALS_AUTOGEN_FXN_NAME)));
		final JassThread myJassThread = globals.createThread(globals.getUserFunctionInstructionPtr("main"));
		globals.queueThread(myJassThread);
		boolean done = false;
		do {
			final long currentTimeMillis = System.currentTimeMillis();
			final Iterator<SleepingData> iterator = sleepingThreadData.iterator();
			while (iterator.hasNext()) {
				final SleepingData next = iterator.next();
				if (currentTimeMillis >= next.wakeTime) {
					next.thread.setSleeping(false);
					iterator.remove();
				}
			}
			done = globals.runThreads();
		}
		while (!done);

		final long end = System.currentTimeMillis();
		System.out.println("Jass executed in: " + (end - start));
	}

}
