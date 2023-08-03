package com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit;

import java.util.Map;

import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CSimulation;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABBooleanCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABFloatCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABUnitCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.core.ABAction;

public class ABActionHeal implements ABAction {

	private ABUnitCallback target;
	private ABFloatCallback amount;
	private ABBooleanCallback isPercent;

	@Override
	public void runAction(CSimulation game, CUnit caster, Map<String, Object> localStore) {
		boolean percent = false;
		if (isPercent != null) {
			percent = isPercent.callback(game, caster, localStore);
		}
		if (percent) {
			CUnit targetUnit = target.callback(game, caster, localStore);
			targetUnit.heal(game, amount.callback(game, caster, localStore) * targetUnit.getMaximumLife());
		} else {
			target.callback(game, caster, localStore).heal(game, amount.callback(game, caster, localStore));
		}
	}

}