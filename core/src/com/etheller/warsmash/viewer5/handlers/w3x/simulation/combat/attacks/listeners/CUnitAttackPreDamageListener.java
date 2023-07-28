package com.etheller.warsmash.viewer5.handlers.w3x.simulation.combat.attacks.listeners;

import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CSimulation;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.CUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilities.targeting.AbilityTarget;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.combat.CAttackType;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.combat.CWeaponType;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.trigger.enumtypes.CDamageType;

public interface CUnitAttackPreDamageListener {
	public static final int PRIORITY_MIN = 0;
	public static final int PRIORITY_MAX = 10;
	public CUnitAttackEffectListenerStacking onAttack(final CSimulation simulation, CUnit attacker, AbilityTarget target, CWeaponType weaponType, CAttackType attackType, CDamageType damageType, CUnitAttackPreDamageListenerDamageModResult damageResult);
}
