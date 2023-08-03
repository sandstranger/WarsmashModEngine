package com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.core;

import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionAddAbility;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionAddStunBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionCreateAbilityFromId;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionCreateBuffFromId;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionCreateSpellEffectAtLocation;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionCreateSpellEffectOnUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionCreateTemporarySpellEffectOnUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionRemoveAbility;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ABActionRemoveSpellEffect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.ability.ABActionStartCooldown;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionAddBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionCreatePassiveBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionCreateTimedBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionCreateTimedTickingBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionCreateTimedTickingPostDeathBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.buff.ABActionRemoveBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionAddDefenseBonus;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionAddNonStackingStatBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionCreateNonStackingStatBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionRecomputeStatBuffsOnUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionRemoveDefenseBonus;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionRemoveNonStackingStatBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.stats.ABActionUpdateNonStackingStatBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionIf;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionIterateUnitsInGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionIterateUnitsInRangeOfUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionIterateUnitsInRect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionPeriodicExecute;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.structural.ABActionStoreValueLocally;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.timer.ABActionCreateTimer;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.timer.ABActionRemoveTimer;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit.ABActionDamageTarget;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit.ABActionHeal;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit.ABActionResurrect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unit.ABActionSetHp;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitgroup.ABActionAddUnitToGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitgroup.ABActionCreateUnitGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitgroup.ABActionRemoveUnitFromGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddAttackPostDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddAttackPreDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddDamageTakenListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddDamageTakenModificationListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddDeathReplacementEffect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionAddEvasionListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateAttackPostDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateAttackPreDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateDamageTakenListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateDamageTakenModificationListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateDeathReplacementEffect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionCreateEvasionListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveAttackPostDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveAttackPreDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveDamageTakenListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveDamageTakenModificationListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveDeathReplacementEffect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.ABActionRemoveEvasionListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionDamageTakenModificationMultiplyDamageMultiplier;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionDamageTakenModificationSetDamageMultiplier;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionDeathReplacementFinishReincarnating;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionDeathReplacementSetReincarnating;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionDeathReplacementSetReviving;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionPreDamageListenerAddBonusDamage;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionPreDamageListenerAddDamageMultiplier;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalActions.ABActionSetPreDamageStacking;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalCallbacks.ABCallbackGetTriggeringAttackType;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalCallbacks.ABCallbackGetTriggeringDamageType;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalCallbacks.ABCallbackIsTriggeringDamageAnAttack;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.action.unitlisteners.internalCallbacks.ABCallbackIsTriggeringDamageRanged;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.abilitycallbacks.ABAbilityCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.abilitycallbacks.ABCallbackGetLastCreatedAbility;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.abilitycallbacks.ABCallbackGetStoredAbilityByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABBooleanCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABCallbackGetAbilityDataAsBoolean;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABCallbackGetParentAbilityDataAsBoolean;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.booleancallbacks.ABCallbackRawBoolean;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.buffcallbacks.ABBuffCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.buffcallbacks.ABCallbackGetLastCreatedBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.buffcallbacks.ABCallbackGetStoredBuffByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABAttackPreDamageListenerPriorityCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABAttackTypeCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABCallbackGetAttackTypeFromString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABCallbackGetDamageTypeFromString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABCallbackGetNonStackingStatBuffTypeFromString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABCallbackRawDeathEffectPriority;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABCallbackRawPreDamageListenerPriority;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABDamageTypeCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABDeathReplacementPriorityCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.enumcallbacks.ABNonStackingStatBuffTypeCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetAbilityArea;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetAbilityDataAsFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetAbilityDuration;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetParentAbilityDataAsFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetStoredFloatByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetUnitFacing;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetUnitLocationX;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackGetUnitLocationY;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackNegativeFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackRandomBoundedFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackRandomFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABCallbackRawFloat;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.floatcallbacks.ABFloatCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.fxcallbacks.ABCallbackGetLastCreatedSpellEffect;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.fxcallbacks.ABCallbackGetStoredFXByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.fxcallbacks.ABFXCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABCallbackGetAlias;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABCallbackGetFirstBuffId;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABCallbackGetParentAlias;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABCallbackGetStoredIDByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABCallbackGetWar3IDFromString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.idcallbacks.ABIDCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.integercallbacks.ABCallbackGetSpellLevel;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.integercallbacks.ABCallbackGetStoredIntegerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.integercallbacks.ABCallbackRawInteger;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.integercallbacks.ABIntegerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABAttackPostDamageListenerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABAttackPreDamageListenerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedAttackPostDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedAttackPreDamageListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedDamageTakenListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedDamageTakenModificationListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedDeathReplacement;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetLastCreatedEvasionListener;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredAttackPostDamageListenerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredAttackPreDamageListenerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredDamageTakenListenerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredDamageTakenModificationListenerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredDeathReplacementByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABCallbackGetStoredEvasionListenerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABDamageTakenListenerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABDamageTakenModificationListenerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABDeathReplacementCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.listenercallbacks.ABEvasionListenerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.statbuffcallbacks.ABCallbackGetLastCreatedNonStackingStatBuff;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.statbuffcallbacks.ABCallbackGetStoredNonStackingStatBuffByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.statbuffcallbacks.ABNonStackingStatBuffCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.stringcallbacks.ABCallbackCatStrings;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.stringcallbacks.ABCallbackGetAllowStackingKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.stringcallbacks.ABCallbackGetUnitHandleAsString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.stringcallbacks.ABCallbackRawString;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.stringcallbacks.ABStringCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.timercallbacks.ABCallbackGetFiringTimer;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.timercallbacks.ABCallbackGetLastCreatedTimer;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.timercallbacks.ABCallbackGetStoredTimerByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.timercallbacks.ABTimerCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetAbilityTargetedUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetAttackedUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetAttackingUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetCastingUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetDyingUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetEnumUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetKillingUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetParentCastingUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABCallbackGetStoredUnitByKey;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitcallbacks.ABUnitCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitgroupcallbacks.ABCallbackGetLastCreatedUnitGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitgroupcallbacks.ABCallbackGetUnitGroupByName;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.callback.unitgroupcallbacks.ABUnitGroupCallback;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.ABConditionIsUnitInGroup;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.ABConditionIsUnitInRangeOfUnit;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.ABConditionIsValidTarget;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.ability.ABConditionIsOnCooldown;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.comparison.ABConditionIsAttackTypeEqual;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.comparison.ABConditionIsDamageTypeEqual;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.comparison.ABConditionIsUnitEqual;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.logical.ABConditionAnd;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.logical.ABConditionBool;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.logical.ABConditionNot;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.logical.ABConditionOr;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatEqual;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatGt;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatGte;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatLt;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatLte;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatNe;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionFloatNe0;
import com.etheller.warsmash.viewer5.handlers.w3x.simulation.abilitybuilder.behavior.condition.numeric.ABConditionIntegerEqual;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbilityBuilderGsonBuilder {

	private static void registerAbilityCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetStoredAbilityByKey.class, "getStoredAbilityByKey")
				.registerSubtype(ABCallbackGetLastCreatedAbility.class, "getLastCreatedAbility");
	}

	private static void registerAttackPostDamageListenerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedAttackPostDamageListener.class,
						"getLastCreatedAttackPostDamageListener")
				.registerSubtype(ABCallbackGetStoredAttackPostDamageListenerByKey.class,
						"getStoredAttackPostDamageListenerByKey");
	}

	private static void registerAttackPreDamageListenerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedAttackPreDamageListener.class,
						"getLastCreatedAttackPreDamageListener")
				.registerSubtype(ABCallbackGetStoredAttackPreDamageListenerByKey.class,
						"getStoredAttackPreDamageListenerByKey");
	}

	private static void registerBooleanCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackRawBoolean.class, "rawBoolean")
				.registerSubtype(ABCallbackGetAbilityDataAsBoolean.class, "getAbilityDataAsBoolean")
				.registerSubtype(ABCallbackGetParentAbilityDataAsBoolean.class, "getParentAbilityDataAsBoolean")
				.registerSubtype(ABCallbackIsTriggeringDamageAnAttack.class, "isTriggeringDamageAnAttack")
				.registerSubtype(ABCallbackIsTriggeringDamageRanged.class, "isTriggeringDamageRanged");
	}

	private static void registerBuffCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetStoredBuffByKey.class, "getStoredBuffByKey")
				.registerSubtype(ABCallbackGetLastCreatedBuff.class, "getLastCreatedBuff");
	}

	private static void registerDamageTakenListenerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedDamageTakenListener.class, "getLastCreatedDamageTakenListener")
				.registerSubtype(ABCallbackGetStoredDamageTakenListenerByKey.class,
						"getStoredDamageTakenListenerByKey");
	}

	private static void registerDamageTakenModificationListenerCallbacks(
			RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedDamageTakenModificationListener.class,
						"getLastCreatedDamageTakenModificationListener")
				.registerSubtype(ABCallbackGetStoredDamageTakenModificationListenerByKey.class,
						"getStoredDamageTakenModificationListenerByKey");
	}

	private static void registerDeathReplacementEffectCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedDeathReplacement.class, "getLastCreatedDeathReplacement")
				.registerSubtype(ABCallbackGetStoredDeathReplacementByKey.class, "getStoredDeathReplacementByKey");
	}

	private static void registerEvasionListenerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedEvasionListener.class, "getLastCreatedEvasionListener")
				.registerSubtype(ABCallbackGetStoredEvasionListenerByKey.class, "getStoredEvasionListenerByKey");
	}

	private static void registerFloatCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetAbilityArea.class, "getAbilityArea")
				.registerSubtype(ABCallbackGetAbilityDuration.class, "getAbilityDuration")
				.registerSubtype(ABCallbackRawFloat.class, "rawFloat")
				.registerSubtype(ABCallbackNegativeFloat.class, "negativeFloat")
				.registerSubtype(ABCallbackGetStoredFloatByKey.class, "getStoredFloatByKey")
				.registerSubtype(ABCallbackGetAbilityDataAsFloat.class, "getAbilityDataAsFloat")
				.registerSubtype(ABCallbackGetParentAbilityDataAsFloat.class, "getParentAbilityDataAsFloat")
				.registerSubtype(ABCallbackRandomFloat.class, "randomFloat")
				.registerSubtype(ABCallbackRandomBoundedFloat.class, "randomBoundedFloat")
				.registerSubtype(ABCallbackGetUnitLocationX.class, "getUnitLocationX")
				.registerSubtype(ABCallbackGetUnitLocationY.class, "getUnitLocationY")
				.registerSubtype(ABCallbackGetUnitFacing.class, "getUnitFacing");
	}

	private static void registerFxCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetLastCreatedSpellEffect.class, "getLastCreatedSpellEffect")
				.registerSubtype(ABCallbackGetStoredFXByKey.class, "getStoredFXByKey");
	}

	private static void registerIdCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetStoredIDByKey.class, "getStoredIDByKey")
				.registerSubtype(ABCallbackGetWar3IDFromString.class, "getWar3IDFromString")
				.registerSubtype(ABCallbackGetAlias.class, "getAlias")
				.registerSubtype(ABCallbackGetParentAlias.class, "getParentAlias")
				.registerSubtype(ABCallbackGetFirstBuffId.class, "getFirstBuffId");
	}

	private static void registerIntegerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackRawInteger.class, "rawInteger")
				.registerSubtype(ABCallbackGetStoredIntegerByKey.class, "getStoredIntegerByKey")
				.registerSubtype(ABCallbackGetSpellLevel.class, "getSpellLevel");
	}

	private static void registerStatBuffCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory
				.registerSubtype(ABCallbackGetLastCreatedNonStackingStatBuff.class, "getLastCreatedNonStackingStatBuff")
				.registerSubtype(ABCallbackGetStoredNonStackingStatBuffByKey.class,
						"getStoredNonStackingStatBuffByKey");
	}

	private static void registerStringCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackRawString.class, "rawString")
				.registerSubtype(ABCallbackCatStrings.class, "catStrings")
				.registerSubtype(ABCallbackGetUnitHandleAsString.class, "getUnitHandleAsString")
				.registerSubtype(ABCallbackGetAllowStackingKey.class, "getAllowStackingKey");
	}

	private static void registerTimerCallbacks(RuntimeTypeAdapterFactory callbackTypeFactory) {
		callbackTypeFactory.registerSubtype(ABCallbackGetLastCreatedTimer.class, "getLastCreatedTimer")
				.registerSubtype(ABCallbackGetFiringTimer.class, "getFiringTimer")
				.registerSubtype(ABCallbackGetStoredTimerByKey.class, "getStoredTimerByKey");
	}

	private static void registerUnitCallbacks(RuntimeTypeAdapterFactory unitCallbackTypeFactory) {
		unitCallbackTypeFactory.registerSubtype(ABCallbackGetCastingUnit.class, "getCastingUnit")
				.registerSubtype(ABCallbackGetEnumUnit.class, "getEnumUnit")
				.registerSubtype(ABCallbackGetAttackedUnit.class, "getAttackedUnit")
				.registerSubtype(ABCallbackGetAttackingUnit.class, "getAttackingUnit")
				.registerSubtype(ABCallbackGetDyingUnit.class, "getDyingUnit")
				.registerSubtype(ABCallbackGetKillingUnit.class, "getKillingUnit")
				.registerSubtype(ABCallbackGetAbilityTargetedUnit.class, "getAbilityTargetedUnit")
				.registerSubtype(ABCallbackGetStoredUnitByKey.class, "getStoredUnitByKey")
				.registerSubtype(ABCallbackGetParentCastingUnit.class, "getParentCastingUnit");
	}

	private static void registerUnitGroupCallbacks(RuntimeTypeAdapterFactory unitGroupCallbackTypeFactory) {
		unitGroupCallbackTypeFactory.registerSubtype(ABCallbackGetUnitGroupByName.class, "getUnitGroupByName")
				.registerSubtype(ABCallbackGetLastCreatedUnitGroup.class, "getLastCreatedUnitGroup");
	}

	private static void registerConditions(RuntimeTypeAdapterFactory<ABCondition> conditionTypeFactory) {
		conditionTypeFactory.registerSubtype(ABConditionAnd.class, "and").registerSubtype(ABConditionOr.class, "or")
				.registerSubtype(ABConditionNot.class, "not").registerSubtype(ABConditionBool.class, "bool")
				.registerSubtype(ABConditionFloatEqual.class, "floatEq")
				.registerSubtype(ABConditionFloatGte.class, "floatGte")
				.registerSubtype(ABConditionFloatGt.class, "floatGt")
				.registerSubtype(ABConditionFloatLte.class, "floatLte")
				.registerSubtype(ABConditionFloatLt.class, "floatLt")
				.registerSubtype(ABConditionFloatNe.class, "floatNe")
				.registerSubtype(ABConditionFloatNe0.class, "floatNe0")
				.registerSubtype(ABConditionIntegerEqual.class, "integerEqual")
				.registerSubtype(ABConditionIsValidTarget.class, "isValidTarget")
				.registerSubtype(ABConditionIsUnitInRangeOfUnit.class, "isUnitInRangeOfUnit")
				.registerSubtype(ABConditionIsUnitInGroup.class, "isUnitInGroup")
				.registerSubtype(ABConditionIsUnitEqual.class, "isUnitEqual")
				.registerSubtype(ABConditionIsAttackTypeEqual.class, "isAttackTypeEqual")
				.registerSubtype(ABConditionIsDamageTypeEqual.class, "isDamageTypeEqual")

				.registerSubtype(ABConditionIsOnCooldown.class, "isOnCooldown");
	}

	private static void registerActions(RuntimeTypeAdapterFactory<ABAction> factory) {
		factory.registerSubtype(ABActionIf.class, "if").registerSubtype(ABActionAddAbility.class, "addAbility")
				.registerSubtype(ABActionCreateSpellEffectOnUnit.class, "createSpellEffectOnUnit")
				.registerSubtype(ABActionCreateTemporarySpellEffectOnUnit.class, "createTemporarySpellEffectOnUnit")
				.registerSubtype(ABActionCreateSpellEffectAtLocation.class, "createSpellEffectAtLocation")
				.registerSubtype(ABActionIterateUnitsInRangeOfUnit.class, "iterateUnitsInRangeOfUnit")
				.registerSubtype(ABActionIterateUnitsInRect.class, "iterateUnitsInRect")
				.registerSubtype(ABActionPeriodicExecute.class, "periodicExecute")
				.registerSubtype(ABActionRemoveAbility.class, "removeAbility")
				.registerSubtype(ABActionStoreValueLocally.class, "storeValueLocally")
				.registerSubtype(ABActionRemoveSpellEffect.class, "removeSpellEffect")
				.registerSubtype(ABActionCreateAbilityFromId.class, "createAbilityFromId")
				.registerSubtype(ABActionCreateBuffFromId.class, "createBuffFromId")
				.registerSubtype(ABActionAddDefenseBonus.class, "addDefenseBonus")
				.registerSubtype(ABActionRemoveDefenseBonus.class, "removeDefenseBonus")
				.registerSubtype(ABActionCreateUnitGroup.class, "createUnitGroup")
				.registerSubtype(ABActionIterateUnitsInGroup.class, "iterateUnitsInGroup")
				.registerSubtype(ABActionAddUnitToGroup.class, "addUnitToGroup")
				.registerSubtype(ABActionRemoveUnitFromGroup.class, "removeUnitFromGroup")
				.registerSubtype(ABActionDamageTarget.class, "damageTarget").registerSubtype(ABActionHeal.class, "heal")
				.registerSubtype(ABActionSetHp.class, "setHp")
				.registerSubtype(ABActionResurrect.class, "resurrect").registerSubtype(ABActionAddStunBuff.class, "addStunBuff")
				.registerSubtype(ABActionStartCooldown.class, "startCooldown")

				.registerSubtype(ABActionCreateTimer.class, "createTimer")
				.registerSubtype(ABActionRemoveTimer.class, "removeTimer")

				.registerSubtype(ABActionAddBuff.class, "addBuff")
				.registerSubtype(ABActionRemoveBuff.class, "removeBuff")
				.registerSubtype(ABActionCreatePassiveBuff.class, "createPassiveBuff")
				.registerSubtype(ABActionCreateTimedBuff.class, "createTimedBuff")
				.registerSubtype(ABActionCreateTimedTickingBuff.class, "createTimedTickingBuff")
				.registerSubtype(ABActionCreateTimedTickingPostDeathBuff.class, "createTimedTickingPostDeathBuff")

				.registerSubtype(ABActionCreateNonStackingStatBuff.class, "createNonStackingStatBuff")
				.registerSubtype(ABActionAddNonStackingStatBuff.class, "addNonStackingStatBuff")
				.registerSubtype(ABActionRemoveNonStackingStatBuff.class, "removeNonStackingStatBuff")
				.registerSubtype(ABActionUpdateNonStackingStatBuff.class, "updateNonStackingStatBuff")
				.registerSubtype(ABActionRecomputeStatBuffsOnUnit.class, "recomputeStatBuffsOnUnit")

				.registerSubtype(ABActionCreateAttackPostDamageListener.class, "createAttackPostDamageListener")
				.registerSubtype(ABActionAddAttackPostDamageListener.class, "addAttackPostDamageListener")
				.registerSubtype(ABActionRemoveAttackPostDamageListener.class, "removeAttackPostDamageListener")
				.registerSubtype(ABActionCreateAttackPreDamageListener.class, "createAttackPreDamageListener")
				.registerSubtype(ABActionAddAttackPreDamageListener.class, "addAttackPreDamageListener")
				.registerSubtype(ABActionRemoveAttackPreDamageListener.class, "removeAttackPreDamageListener")
				.registerSubtype(ABActionCreateDamageTakenListener.class, "createDamageTakenListener")
				.registerSubtype(ABActionAddDamageTakenListener.class, "addDamageTakenListener")
				.registerSubtype(ABActionRemoveDamageTakenListener.class, "removeDamageTakenListener")
				.registerSubtype(ABActionCreateDamageTakenModificationListener.class,
						"createDamageTakenModificationListener")
				.registerSubtype(ABActionAddDamageTakenModificationListener.class, "addDamageTakenModificationListener")
				.registerSubtype(ABActionRemoveDamageTakenModificationListener.class,
						"removeDamageTakenModificationListener")
				.registerSubtype(ABActionCreateEvasionListener.class, "createEvasionListener")
				.registerSubtype(ABActionAddEvasionListener.class, "addEvasionListener")
				.registerSubtype(ABActionRemoveEvasionListener.class, "removeEvasionListener")
				.registerSubtype(ABActionCreateDeathReplacementEffect.class, "createDeathReplacementEffect")
				.registerSubtype(ABActionAddDeathReplacementEffect.class, "addDeathReplacementEffect")
				.registerSubtype(ABActionRemoveDeathReplacementEffect.class, "removeDeathReplacementEffect")

				.registerSubtype(ABActionDamageTakenModificationSetDamageMultiplier.class, "setDamageTakenMultiplier")
				.registerSubtype(ABActionDamageTakenModificationMultiplyDamageMultiplier.class,
						"multiplyDamageTakenMultiplier")
				.registerSubtype(ABActionPreDamageListenerAddDamageMultiplier.class, "addDamageDealtMultiplier")
				.registerSubtype(ABActionPreDamageListenerAddBonusDamage.class, "addBonusDamageDealt")
				.registerSubtype(ABActionSetPreDamageStacking.class, "setStacking")
				.registerSubtype(ABActionDeathReplacementSetReviving.class, "setReviving")
				.registerSubtype(ABActionDeathReplacementSetReincarnating.class, "setReincarnating")
				.registerSubtype(ABActionDeathReplacementFinishReincarnating.class, "finishReincarnating");
	}

	public static Gson create() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		final RuntimeTypeAdapterFactory<ABAction> actionTypeFactory = RuntimeTypeAdapterFactory.of(ABAction.class,
				"type");
		registerActions(actionTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(actionTypeFactory);

		final RuntimeTypeAdapterFactory<ABCallback> callbackTypeFactory = RuntimeTypeAdapterFactory.of(ABCallback.class,
				"type");
		registerAbilityCallbacks(callbackTypeFactory);
		registerAttackPostDamageListenerCallbacks(callbackTypeFactory);
		registerAttackPreDamageListenerCallbacks(callbackTypeFactory);
		registerBooleanCallbacks(callbackTypeFactory);
		registerBuffCallbacks(callbackTypeFactory);
		registerDamageTakenListenerCallbacks(callbackTypeFactory);
		registerDamageTakenModificationListenerCallbacks(callbackTypeFactory);
		registerDeathReplacementEffectCallbacks(callbackTypeFactory);
		registerEvasionListenerCallbacks(callbackTypeFactory);
		registerFloatCallbacks(callbackTypeFactory);
		registerFxCallbacks(callbackTypeFactory);
		registerIdCallbacks(callbackTypeFactory);
		registerIntegerCallbacks(callbackTypeFactory);
		registerStatBuffCallbacks(callbackTypeFactory);
		registerStringCallbacks(callbackTypeFactory);
		registerTimerCallbacks(callbackTypeFactory);
		registerUnitCallbacks(callbackTypeFactory);
		registerUnitGroupCallbacks(callbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(callbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABAbilityCallback> abilityCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABAbilityCallback.class, "type");
		registerAbilityCallbacks(abilityCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(abilityCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABAttackPostDamageListenerCallback> attackPostDamageListenerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABAttackPostDamageListenerCallback.class, "type");
		registerAttackPostDamageListenerCallbacks(attackPostDamageListenerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(attackPostDamageListenerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABAttackPreDamageListenerCallback> attackPreDamageListenerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABAttackPreDamageListenerCallback.class, "type");
		registerAttackPreDamageListenerCallbacks(attackPreDamageListenerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(attackPreDamageListenerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABBooleanCallback> booleanCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABBooleanCallback.class, "type");
		registerBooleanCallbacks(booleanCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(booleanCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABBuffCallback> buffCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABBuffCallback.class, "type");
		registerBuffCallbacks(buffCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(buffCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABDamageTakenListenerCallback> damageTakenListenerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABDamageTakenListenerCallback.class, "type");
		registerDamageTakenListenerCallbacks(damageTakenListenerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(damageTakenListenerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABDamageTakenModificationListenerCallback> damageTakenModificationListenerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABDamageTakenModificationListenerCallback.class, "type");
		registerDamageTakenModificationListenerCallbacks(damageTakenModificationListenerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(damageTakenModificationListenerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABDeathReplacementCallback> deathReplacementEffectCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABDeathReplacementCallback.class, "type");
		registerDeathReplacementEffectCallbacks(deathReplacementEffectCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(deathReplacementEffectCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABEvasionListenerCallback> evasionListenerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABEvasionListenerCallback.class, "type");
		registerEvasionListenerCallbacks(evasionListenerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(evasionListenerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABFloatCallback> floatCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABFloatCallback.class, "type");
		registerFloatCallbacks(floatCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(floatCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABFXCallback> fxCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABFXCallback.class, "type");
		registerFxCallbacks(fxCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(fxCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABIDCallback> idCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABIDCallback.class, "type");
		registerIdCallbacks(idCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(idCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABIntegerCallback> integerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABIntegerCallback.class, "type");
		registerIntegerCallbacks(integerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(integerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABNonStackingStatBuffCallback> statBuffCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABNonStackingStatBuffCallback.class, "type");
		registerStatBuffCallbacks(statBuffCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(statBuffCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABStringCallback> stringCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABStringCallback.class, "type");
		registerStringCallbacks(stringCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(stringCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABTimerCallback> timerCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABTimerCallback.class, "type");
		registerTimerCallbacks(timerCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(timerCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABUnitCallback> unitCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABUnitCallback.class, "type");
		registerUnitCallbacks(unitCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(unitCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABUnitGroupCallback> unitGroupCallbackTypeFactory = RuntimeTypeAdapterFactory
				.of(ABUnitGroupCallback.class, "type");
		registerUnitGroupCallbacks(unitGroupCallbackTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(unitGroupCallbackTypeFactory);

		final RuntimeTypeAdapterFactory<ABCondition> conditionTypeFactory = RuntimeTypeAdapterFactory
				.of(ABCondition.class, "type");
		registerConditions(conditionTypeFactory);
		gsonBuilder.registerTypeAdapterFactory(conditionTypeFactory);

		// ENUM CALLBACKS
		final RuntimeTypeAdapterFactory<ABNonStackingStatBuffTypeCallback> nssbtTypeFactory = RuntimeTypeAdapterFactory
				.of(ABNonStackingStatBuffTypeCallback.class, "type");
		nssbtTypeFactory.registerSubtype(ABCallbackGetNonStackingStatBuffTypeFromString.class,
				"getNonStackingStatBuffTypeFromString");
		gsonBuilder.registerTypeAdapterFactory(nssbtTypeFactory);

		final RuntimeTypeAdapterFactory<ABAttackTypeCallback> atTypeFactory = RuntimeTypeAdapterFactory
				.of(ABAttackTypeCallback.class, "type");
		atTypeFactory.registerSubtype(ABCallbackGetAttackTypeFromString.class, "getAttackTypeFromString")
				.registerSubtype(ABCallbackGetTriggeringAttackType.class, "getTriggeringAttackType");
		gsonBuilder.registerTypeAdapterFactory(atTypeFactory);

		final RuntimeTypeAdapterFactory<ABDamageTypeCallback> dtTypeFactory = RuntimeTypeAdapterFactory
				.of(ABDamageTypeCallback.class, "type");
		dtTypeFactory.registerSubtype(ABCallbackGetDamageTypeFromString.class, "getDamageTypeFromString")
				.registerSubtype(ABCallbackGetTriggeringDamageType.class, "getTriggeringDamageType");
		gsonBuilder.registerTypeAdapterFactory(dtTypeFactory);

		final RuntimeTypeAdapterFactory<ABAttackPreDamageListenerPriorityCallback> pdlpTypeFactory = RuntimeTypeAdapterFactory
				.of(ABAttackPreDamageListenerPriorityCallback.class, "type");
		pdlpTypeFactory.registerSubtype(ABCallbackRawPreDamageListenerPriority.class, "rawPriority");
		gsonBuilder.registerTypeAdapterFactory(pdlpTypeFactory);

		final RuntimeTypeAdapterFactory<ABDeathReplacementPriorityCallback> drepTypeFactory = RuntimeTypeAdapterFactory
				.of(ABDeathReplacementPriorityCallback.class, "type");
		drepTypeFactory.registerSubtype(ABCallbackRawDeathEffectPriority.class, "rawPriority");
		gsonBuilder.registerTypeAdapterFactory(drepTypeFactory);

		return gsonBuilder.create();
	}
}