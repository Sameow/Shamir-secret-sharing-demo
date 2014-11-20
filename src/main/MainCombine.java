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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import engine.SecretShare;
import engine.SecretShare.PublicInfo;
import engine.SecretShare.ShareInfo;
import exceptions.SecretShareException;
import math.BigIntUtilities;

/**
 * Main command line for the "combine" (aka "recover") of a secret.
 *
 * Takes a threshold (k), and a modulus [if any],
 *  and "k" secrets with their index,
 *  and recovers the original secret.
 *
 * @author tiemens
 *
 */
public final class MainCombine
{

    /**
     * @param args from command line
     */
    public static void main(String[] args)
    {
//        main(args, System.in, System.out);
    }

    public static BigInteger combine(String[] args)
    {
    	BigInteger fileBigInt = null;
        try
        {
            CombineInput input = CombineInput.parse(args);
            CombineOutput output = input.output();
            fileBigInt = output.retrieveSecret();
        }
        catch (SecretShareException e)
        {
            System.out.println(e.getMessage());
//            usage(out);
//            MainSplit.optionallyPrintStackTrace(args, e, out);
        }
        return fileBigInt;
    }

    public static void usage(PrintStream out)
    {
        out.println("Usage:");
        out.println(" combine -k <k>  -s<n> <secret-N> ..." +               // required
                    "  [-primeCustom] -stdin"); // optional
        out.println("  -k <k>        the threshold");
        out.println("  -s<n> <s>     secret#n as a number e.g. '124332' or 'bigintcs:123456-DC0AE1'");
        out.println("     ...           repeat this argument <k> times");
        out.println("  -stdin        read values from standard input, as written by 'split'");
    }

    private static BigInteger parseBigInteger(String argname,
                                              String[] args,
                                              int index)
    {
        return MainSplit.parseBigInteger(argname, args, index);
    }

    private static Integer parseInt(String argname,
                                    String[] args,
                                    int index)
    {
        return MainSplit.parseInt(argname, args, index);
    }

    private MainCombine()
    {
        // no instances
    }



    public static class CombineInput
    {
        // ==================================================
        // instance data
        // ==================================================

        // required arguments:
        private Integer k           = null;

        private final List<SecretShare.ShareInfo> shares = new ArrayList<SecretShare.ShareInfo>();

        private BigInteger modulus = null;

        // optional: for combine, we don't need n, but you can provide it
        private Integer n           = null;


        // not an input.  used to cache the PublicInfo, so that after the first ShareInfo is
        //  created with this PublicInfo, then they are all created with the same PublicInfo
        private PublicInfo publicInfo;

        // ==================================================
        // constructors
        // ==================================================
        public static CombineInput parse(String[] args)
        {
            CombineInput ret = new CombineInput();

            for (int i = 0, n = args.length; i < n; i++)
            {
                if (args[i] == null)
                {
                    continue;
                }

                if ("-k".equals(args[i]))
                {
                    i++;
                    ret.k = parseInt("k", args, i);
                    if (ret.n == null)
                    {
                        ret.n = ret.k;
                    }
                }
                else if ("-n".equals(args[i]))
                {
                    i++;
                    ret.n = parseInt("n", args, i);
                }
                else if ("-m".equals(args[i]))
                {
                    i++;
                    ret.modulus = parseBigInteger("m", args, i);
                }
                else if ("-primeN".equals(args[i]))
                {
                    i++;
                    ret.modulus = parseBigInteger("primeN", args, i);
                }
                else if ("-primeNone".equals(args[i]))
                {
                    ret.modulus = null;
                }
                else if (args[i].startsWith("-s"))
                {
                    String number = args[i].substring(2);
                    i++;
                    MainSplit.checkIndex("s", args, i);
                    // put in "standard" format and parse that string:
                    String line = "Share (x:" + number + ") = " + args[i];
                    SecretShare.ShareInfo share = ret.parseEqualShare("-s", line);

                    ret.addIfNotDuplicate(share);
                }
                else if (args[i].startsWith("-"))
                {
                    String m = "Argument '" + args[i] + "' not understood";
                    throw new SecretShareException(m);
                }
                else
                {
                    String m = "Extra argument '" + args[i] + "' not valid";
                    throw new SecretShareException(m);
                }
            }
            checkRequired("-k", ret.k);
            if (ret.shares.size() < ret.k)
            {
                throw new SecretShareException("k set to " + ret.k + " but only " +
                                               ret.shares.size() + " shares provided");
            }

            return ret;
        }

        

        // examples of the kinds of lines we look for:

        private void addIfNotDuplicate(ShareInfo add)
        {
            boolean shouldadd = true;
            for (ShareInfo share : this.shares)
            {
                if (share.getX() == add.getX())
                {
                    // dupe
                    if (! share.getShare().equals(add.getShare()))
                    {
                        throw new SecretShareException("share x:" + share.getX() +
                                                       " was entered with two different values " +
                                                       "(" + share.getShare() + ") and (" +
                                                       add.getShare() + ")");
                    }
                    else
                    {
                        shouldadd = false;
                    }
                }
                else if (share.getShare().equals(add.getShare()))
                {
                    throw new SecretShareException("duplicate share values at x:" +
                                                   share.getX() + " and x:" +
                                                   add.getX());
                }
            }
            if (shouldadd)
            {
                this.shares.add(add);
            }
        }


        /**
         *
         * @param fieldname description of source of data
         * @param line is "standard format for share", example:
         *   Share (x:2) = 481883688232565050752267350226995441999530323860
         * @return ShareInfo (integer and big integer)
         */
        private ShareInfo parseEqualShare(String fieldname,
                                          String line)
        {
            if (this.publicInfo == null)
            {
                this.publicInfo = constructPublicInfoFromFields("parseEqualShare");
            }

            BigInteger s = parseEqualBigInt(fieldname, line);
            int x = parseXcolon(line);
            return new ShareInfo(x, s, this.publicInfo);
        }

        private PublicInfo constructPublicInfoFromFields(String where)
        {
            return new SecretShare.PublicInfo(this.n, this.k, this.modulus,
                                              "MainCombine:" + where);
        }

        //  Share (x:2) = bigintcs:005468-69732d-4e02c5-7b11d2-9d4426-e26c88-8a6f94-9809A9
        private int parseXcolon(String line)
        {
            String i = after(line, ":");
            int end = i.indexOf(")");
            i = i.substring(0, end);

            return Integer.valueOf(i);
        }

        private BigInteger parseEqualBigInt(String fieldname,
                                            String line)
        {
            String s = after(line, "=");
            if (BigIntUtilities.Checksum.couldCreateFromStringMd5CheckSum(s))
            {
                return BigIntUtilities.Checksum.createBigInteger(s);
            }
            else if (BigIntUtilities.Hex.couldCreateFromStringHex(s))
            {
                return BigIntUtilities.Hex.createBigInteger(s);
            }
            else
            {
                return new BigInteger(s);
            }
        }

        private String after(String line,
                             String lookfor)
        {
            return line.substring(line.indexOf(lookfor) + 1).trim();
        }

        private static void checkRequired(String argname,
                                          Object obj)
        {
            if (obj == null)
            {
                throw new SecretShareException("Argument '" + argname + "' is required.");
            }
        }

        // ==================================================
        // public methods
        // ==================================================
        public CombineOutput output()
        {
            CombineOutput ret = new CombineOutput();
            ret.combineInput = this;

            // it is a "copy" since it should be equal to this.publicInfo
            SecretShare.PublicInfo copyPublicInfo = constructPublicInfoFromFields("output");

            SecretShare secretShare = new SecretShare(copyPublicInfo);

            SecretShare.CombineOutput combine = secretShare.combine(shares);

            ret.secret = combine.getSecret();

            return ret;
        }

        // ==================================================
        // non public methods
        // ==================================================
    }

    public static class CombineOutput
    {
        private BigInteger secret;

        @SuppressWarnings("unused")
        private SecretShare.CombineOutput combineOutput;
        @SuppressWarnings("unused")
        private CombineInput combineInput;

        public void print(PrintStream out)
        {
            //final SecretShare.PublicInfo publicInfo = combineOutput.getPublicInfo();

            out.println("Secret Share version ");
            //field(out, "Date", publicInfo.getDate());
            //field(out, "UUID", publicInfo.getUuid());
            //field(out, "Description", publicInfo.getDescription());

            out.println("secret.number = '" + secret + "'");
            String s = BigIntUtilities.Human.createHumanString(secret);
            out.println("secret.string = '" + s + "'");

        }
        
        public BigInteger retrieveSecret(){
        	return secret;
        }

        // ==================================================
        // instance data
        // ==================================================

        // ==================================================
        // constructors
        // ==================================================

        // ==================================================
        // public methods
        // ==================================================

        // ==================================================
        // non public methods
        // ==================================================
    }
}
