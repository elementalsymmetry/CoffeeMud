package com.planet_ink.coffee_mud.Items.Weapons;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;


public class Natural extends StdWeapon
{
	public String ID(){	return "Natural";}
	public Natural()
	{
		super();

		setName("fingernails and teeth");
		setDisplayText("A set of claws and teeth are piled here.");
		setDescription("Those hands and claws look fit to kill.");
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats.setWeight(0);
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(0);
		weaponType=TYPE_NATURAL;
		recoverEnvStats();
		material=EnvResource.RESOURCE_SCALES;
		weaponClassification=Weapon.CLASS_NATURAL;
	}


	public String hitString(int damageAmount)
	{
		return "<S-NAME> "+CommonStrings.standardHitWord(weaponType,damageAmount)+" <T-NAMESELF>";
	}
}
