package com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit;

import java.util.Map;

import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CSimulation;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABBooleanCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABFloatCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABUnitCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.core.ABAction;

public class ABActionSetMp implements ABAction {

	private ABUnitCallback target;
	private ABFloatCallback amount;
	private ABBooleanCallback isPercent;

	@Override
	public void runAction(CSimulation game, CUnit caster, Map<String, Object> localStore, final int castId) {
		boolean percent = false;
		if (isPercent != null) {
			percent = isPercent.callback(game, caster, localStore, castId);
		}
		if (percent) {
			CUnit targetUnit = target.callback(game, caster, localStore, castId);
			targetUnit.setMana(amount.callback(game, caster, localStore, castId) * targetUnit.getMaximumMana());
		} else {
			target.callback(game, caster, localStore, castId).setMana(amount.callback(game, caster, localStore, castId));
		}
	}

}
