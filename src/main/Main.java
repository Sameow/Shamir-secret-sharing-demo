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

import buildVer.BuildVersion;

/**
 * "Dispatch" main program.
 * Based on the first argument in args[], this calls one of:
 *   split
 *   combine
 *
 * @author tiemens
 *
 */
public final class Main
{
    /**
     * @param args from command line
     */
    public static void main(String[] args)
    {
    	//TOGGLE MODE HERE
    	String[] arguments={"split", "-k", "3", "-n", "6", "-sS", 
    	"This is a very very very extremely long string that is longer than the average string blah blah blah gotta make this even longer. give me a second. I'm gonna eat prata", 
    	"-primeCustom"		
    	};
//    	String[] arguments={"combine", "-k", "3", "-primeN", "2503279904313430979526441539986226210698189892005409343187043532420404883210960433283500108290944160472477959095691936599047519",
//    	"-s1", 
//    	"4427704511336946319606669281948634209194686719310745334832088015071836414277346320293758696896656257536453850059987683645020246740813525218095301036022388675655633890016552494444790170077648175340220470140336572342912562548531785284235949248",
//    	"-s2", 
//    	"7619160730657585685872856308267703750908129279261229257333519626976637956472271425522450633834506379516961553578930344598966722391122754235436853657569026856896563323857591066588158853404678714521807611700354255155474537934527700032764936802",
//    	"-s3", 
//    	"11772934370045574760608423039845777599852963280481618927579004552210498192909085908934948383653766005154948008983738922578590750702870233600673635083747164354713289060282202159727731652510581382729896065623713996962538330139177272007406783054",
//    	};
        main(arguments, System.in, System.out, true);
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
                MainSplit.main(args, in, out);
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

    private Main()
    {
        // no instances
    }

    // ==================================================
    // public methods
    // ==================================================

    // ==================================================
    // non public methods
    // ==================================================
}
