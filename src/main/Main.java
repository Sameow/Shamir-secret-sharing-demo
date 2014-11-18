/*******************************************************************************
 * $Id: $
 * Copyright (c) 2009-2010 Tim Tiemens.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 *
 * Contributors:
 *     Tim Tiemens - initial API and implementation
 ******************************************************************************/
package main;

import java.io.InputStream;
import java.io.PrintStream;
import main.FileSplit;
import main.ShamirShare;

/**
 * "Dispatch" main program.
 * Based on the first argument in args[], this calls one of:
 *   split
 *   combine
 *
 * @author tiemens
 *
 */
public class Main
{
	
    /**
     * @param args from command line
     */
    public static void main(String[] args)
    {    	
    	//TOGGLE MODE HERE
    	String[] arguments={"split", "-k", "3", "-n", "6", "-sS", 
    	"This is a very very very extremely long string that is longer than the average string blah blah blah gotta make this even longer. give me a second. I'm gonna eat prata etc etc etc etc", 
    	"-primeCustom"		
    	};
//    	String[] arguments={"combine", "-k", "3", "-primeN", "6930677988635973240143237874196640406187348201131762525744110929896705345195639655105216861300099413593665970374449441580445778993540899007725778799060582622773601851289427783594038280685949642258200169456760953212223292524601716787040065204209296352847823597684749841448723748527539084649637396356075496989015401541852425640770419107349141029363203138933579469069416525745672378281400186669734829286244626037035326851849268225370580766484917",
//    	"-s3", 
//    	"1399772544775873069508552386184083189012747391721779448976809558390548646012853715103462188564359179547370447961024244726154555560280501586328060231492357098069902918356190390923945516309958371199497469311511153823406295709438414005767629318787587682686406539836636094007777697647183445099748670025065710530938176385911459934012221050177246849969760000410009808201030238521511316342215231112959906301598251509767578089003086180406917217462692",
//    	"-s5", 
//    	"3035478713978514795360299727600500628983124036256088704093362018419724436123289260049153164964826747289664899711724418002512329756683388246642040994335768329313317307596028961293758741222797042860028955178517226996312595156605714541335570452870316236313324795081308467756031431506334960271532062120014083527711554530329347973044255905553048716235676078001018238314401558578824374558479261305681071516763991970774255317536078071360268074730518",
//    	"-s6", 
//    	"4097747005617672629287742448659846011049869589343453621830038941295999612535802979078381796709621046696083172061873039135358784010424679465471322099979533960691935177321062027558503085936620334388745754036744348844571926912562321370842138744414976946844625734519553910236389842505367449425512696075260346899891058738984708164456751084405928291451565465004784305136037844335917464750683325649228510780144645807529101022862245557168654996334641",
//    	};
    	
    	//main(arguments, System.in, System.out, true);
        
    }

    public static void main(String[] args,
                            InputStream in,
                            PrintStream out,
                            boolean callExit)
    {	 
        if (args.length < 1)
        {
            out.println("Error: must supply at least 1 argument");
            usage(out);
            if (callExit)
            {
                System.exit(1);
            }
            else
            {
                return;
            }
        }
        else
        {
            String cmd = args[0];
            args[0] = null;
            if ("split".equalsIgnoreCase(cmd))
            {
               
            }
            else if ("combine".equalsIgnoreCase(cmd))
            {
                MainCombine.main(args, in, out);
            }
            else
            {
                out.println("Error: could not understand argument '" + cmd + "' - it must be " +
                                   "either 'split', 'combine', 'info' or 'bigintcs'");
                usage(out);
                if (callExit)
                {
                    System.exit(1);
                }
                else
                {
                    return;
                }
            }
        }

    }


    private static void usage(PrintStream out)
    {
        out.println("Usage:  java -jar secretshare.jar <split>|<combine>");
    }

    // ==================================================
    // class static data
    // ==================================================

    // ==================================================
    // class static methods
    // ==================================================

    // ==================================================
    // instance data
    // ==================================================

    // ==================================================
    // factories
    // ==================================================

    // ==================================================
    // constructors
    // ==================================================


}
