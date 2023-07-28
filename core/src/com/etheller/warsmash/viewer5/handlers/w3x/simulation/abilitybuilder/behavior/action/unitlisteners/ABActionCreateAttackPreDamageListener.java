
package com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners;

import java.util.List;
import java.util.Map;

import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CSimulation;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.core.ABAction;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.core.ABLocalStoreKeys;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.listener.ABAttackPreDamageListener;

public class ABActionCreateAttackPreDamageListener implements ABAction {

	private List<ABAction> actions;

	public void runAction(final CSimulation game, final CUnit caster, final Map<String, Object> localStore) {
		ABAttackPreDamageListener listener = new ABAttackPreDamageListener(localStore, actions);

		localStore.put(ABLocalStoreKeys.LASTCREATEDAPrDL, listener);
	}
}