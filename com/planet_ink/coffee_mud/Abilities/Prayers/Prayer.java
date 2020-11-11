package com.planet_ink.coffee_mud.Abilities.Prayers;
import com.planet_ink.coffee_mud.Abilities.StdAbility;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/*
   Copyright 2001-2020 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Prayer extends StdAbility
{
	@Override
	public String ID()
	{
		return "Prayer";
	}

	private final static String	localizedName	= CMLib.lang().L("a Prayer");

	@Override
	public String name()
	{
		return localizedName;
	}

	@Override
	public String displayText()
	{
		return "";
	}

	@Override
	protected int canAffectCode()
	{
		return 0;
	}

	@Override
	protected int canTargetCode()
	{
		return CAN_MOBS;
	}

	@Override
	public int abstractQuality()
	{
		return Ability.QUALITY_INDIFFERENT;
	}

	private static final String[]	triggerStrings	= I(new String[] { "PRAY", "PR" });

	@Override
	public String[] triggerStrings()
	{
		return triggerStrings;
	}

	@Override
	public int classificationCode()
	{
		return Ability.ACODE_PRAYER;
	}

	protected String prayWord(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return "pray(s) to "+mob.charStats().deityName();
		return "pray(s)";
	}

	protected String prayForWord(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return "pray(s) for "+mob.charStats().deityName();
		return "pray(s)";
	}

	protected String inTheNameOf(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return " in the name of "+mob.charStats().deityName();
		return "";
	}

	protected String againstTheGods(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return " against "+mob.charStats().deityName();
		return " against the gods";
	}

	protected String hisHerDiety(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return mob.charStats().deityName();
		return "<S-HIS-HER> god";
	}

	protected String ofDiety(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return " of "+mob.charStats().deityName();
		return "";
	}

	protected String prayingWord(final MOB mob)
	{
		if(mob.charStats().deityName().length()>0)
			return "praying to "+mob.charStats().deityName();
		return "praying";
	}

	protected static List<Ability> getRelicPrayers(final Physical P)
	{
		final List<Ability> prayV=new Vector<Ability>();
		if(P instanceof Wand)
		{
			final Ability A=((Wand)P).getSpell();
			if((A!=null)&&((A.classificationCode()&Ability.ALL_ACODES)==Ability.ACODE_PRAYER))
				prayV.add(A);
		}
		else
		if((P instanceof Item)
		&&(!(P instanceof Weapon))
		&&(!(P instanceof Armor))
		&&(!(P instanceof Scroll)))
		{
			for(final Enumeration<Ability> a= ((Item)P).effects(); a.hasMoreElements();)
			{
				final Ability A=a.nextElement();
				if((A instanceof AbilityContainer)
				&&(A instanceof Dischargeable))
				{
					final AbilityContainer aCont = (AbilityContainer)A;
					for(final Enumeration<Ability> suba = aCont.abilities();suba.hasMoreElements();)
					{
						final Ability subA=suba.nextElement();
						if(subA instanceof Prayer)
							prayV.add(subA);
					}
				}
			}
		}
		return prayV;
	}

	protected static Dischargeable getDischargeableRelic(final Physical P)
	{
		if(P instanceof Wand)
			return (Wand)P;
		else
		if((P instanceof Item)
		&&(!(P instanceof Weapon))
		&&(!(P instanceof Armor))
		&&(!(P instanceof Scroll)))
		{
			for(final Enumeration<Ability> a= ((Item)P).effects(); a.hasMoreElements();)
			{
				final Ability A=a.nextElement();
				if((A instanceof AbilityContainer)
				&&(A instanceof Dischargeable))
				{
					return (Dischargeable)A;
				}
			}
		}
		return null;
	}

	protected static void setRelicCharges(final Physical P, final int num)
	{
		if(Prayer.isARelic(P))
		{
			final Dischargeable D=getDischargeableRelic(P);
			if(D != null)
				D.setCharges(num);
		}
	}

	protected static void clearRelicMagic(final Physical P)
	{
		if(Prayer.isARelic(P))
		{
			final Dischargeable D=getDischargeableRelic(P);
			if(D != null)
			{
				if(D instanceof Ability)
					P.delEffect((Ability)D);
				else
				if(D instanceof Wand)
					((Wand)D).setSpell(null);
			}
		}
	}

	protected static int getRelicCharges(final Physical P)
	{
		if(Prayer.isARelic(P))
		{
			final Dischargeable D=getDischargeableRelic(P);
			if(D != null)
				return D.getCharges();
		}
		return -1;
	}

	protected static boolean isARelic(final Physical P)
	{
		return getRelicPrayers(P).size()>0;
	}

	protected static boolean prayerAlignmentCheck(final StdAbility A, final MOB mob, final boolean auto)
	{
		if((!auto)
		&&(!mob.isMonster())
		&&(!A.disregardsArmorCheck(mob))
		&&(mob.isMine(A))
		&&(!A.appropriateToMyFactions(mob))
		&&(A.flags()!=0))
		{
			int hq=500;
			if(CMath.bset(A.flags(),Ability.FLAG_HOLY))
			{
				if(!CMath.bset(A.flags(),Ability.FLAG_UNHOLY))
					hq=1000;
			}
			else
			if(CMath.bset(A.flags(),Ability.FLAG_UNHOLY))
				hq=0;

			int basis=0;
			if(hq==0)
				basis=CMLib.factions().getAlignPurity(mob.fetchFaction(CMLib.factions().getAlignmentID()),Faction.Align.EVIL);
			else
			if(hq==1000)
				basis=CMLib.factions().getAlignPurity(mob.fetchFaction(CMLib.factions().getAlignmentID()),Faction.Align.GOOD);
			else
			{
				basis=CMLib.factions().getAlignPurity(mob.fetchFaction(CMLib.factions().getAlignmentID()),Faction.Align.NEUTRAL);
				basis-=10;
			}

			if(CMLib.dice().rollPercentage()>basis)
				return true;

			if(hq==0)
				mob.tell(A.L("The evil nature of @x1 disrupts your prayer.",A.name()));
			else
			if(hq==1000)
				mob.tell(A.L("The goodness of @x1 disrupts your prayer.",A.name()));
			else
			if(CMLib.flags().isGood(mob))
				mob.tell(A.L("The anti-good nature of @x1 disrupts your thought.",A.name()));
			else
			if(CMLib.flags().isEvil(mob))
				mob.tell(A.L("The anti-evil nature of @x1 disrupts your thought.",A.name()));
			return false;
		}
		return true;
	}

	@Override
	public boolean invoke(final MOB mob, final List<String> commands, final Physical target, final boolean auto, final int asLevel)
	{
		if(!super.invoke(mob,commands,target,auto,asLevel))
			return false;
		if(!prayerAlignmentCheck(this,mob,auto))
			return false;
		return true;
	}

}
