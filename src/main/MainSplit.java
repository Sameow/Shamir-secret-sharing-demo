/*******************************************************************************
 * Copyright (c) 2009, 2014 Tim Tiemens.
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
 *******************************************************************************/
package main;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.SecretShare;
import engine.SecretShare.ShareInfo;
import engine.SecretShare.SplitSecretOutput;
import exceptions.SecretShareException;
import math.BigIntUtilities;

/**
 * Main command line for the "split" (aka "create") of a secret.
 *
 * Takes a number of shares (n) and a threshold (k)
 *  and a secret (s) and creates the SecretShare.
 *
 * @author tiemens
 *
 */
public final class MainSplit
{

    /**
     * @param args from command line
     */
    public static void main(String[] args)
    {
        //main(args, System.in, System.out);
    }

    public static SplitOutput split(String[] args)
    {
    	 SplitOutput output = null;
        try
        {
            SplitInput input = SplitInput.parse(args);
            output = input.output();
            
        }
        catch (SecretShareException e)
        {
            System.out.println(e.getMessage());
//            usage(out);
//            optionallyPrintStackTrace(args, e, out);
        }
        return output;
    }

    public static void usage(PrintStream out)
    {
        out.println("Usage:");
        out.println(" split -k <k> -n <n> -sN|-sS secret " +     // required
                    "  [-primeCustom] [-d <desc>] "); // optional
        out.println("  -k <k>        the threshold");
        out.println("  -n <k>        the number of shares to generate");
        out.println("  -sN           the secret as a number, e.g. '124332' or 'bigintcs:01e5ac-787852'");
        out.println("  -sS           the secret as a string, e.g. 'My Secret'");
        out.println("  -d <desc>     description of the secret");;
        out.println("  -m <modulus>  for modulus, use <modulus>, e.g. '11753999' or 'bigintcs:b35a0f-F89BEC'");
        out.println("  -printOne     put all shares on 1 sheet of paper");
        out.println("  -printIndiv   put 1 share per sheet, use 'n' sheets of paper");

    }



    public static BigInteger parseBigInteger(String argname,
                                             String[] args,
                                             int index)
    {
        checkIndex(argname, args, index);

        String value = args[index];
        BigInteger ret = null;
        if (BigIntUtilities.Checksum.couldCreateFromStringMd5CheckSum(value))
        {
            try
            {
                ret = BigIntUtilities.Checksum.createBigInteger(value);
            }
            catch (SecretShareException e)
            {
                String m = "Failed to parse 'bigintcs:' because: " + e.getMessage();
                throw new SecretShareException(m, e);
            }
        }
        else
        {
            try
            {
                ret = new BigInteger(value);
            }
            catch (NumberFormatException e)
            {
                String m = "Failed to parse integer because: " + e.getMessage();
                throw new SecretShareException(m, e);
            }
        }

        return ret;
    }

    public static Integer parseInt(String argname,
                                   String[] args,
                                   int index)
    {
        checkIndex(argname, args, index);
        String value = args[index];

        Integer ret = null;
        try
        {
            ret = Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            String m = "The argument of '" + value + "' " +
                               "is not a number.";
            throw new SecretShareException(m, e);
        }
        return ret;
    }


    public static void checkIndex(String argname,
                                  String[] args,
                                  int index)
    {
        if (index >= args.length)
        {
            throw new SecretShareException("The argument '-" + argname + "' requires an " +
                                           "additional argument");
        }
    }

    public static void optionallyPrintStackTrace(String[] args,
                                                 Exception e,
                                                 PrintStream out)
    {
        boolean print = false;
        for (String s : args)
        {
            if (s != null)
            {
                print = true;
            }
        }
        if (print)
        {
            e.printStackTrace(out);
        }
    }

    private MainSplit()
    {
        // no instances
    }


    public static class SplitInput
    {
        // ==================================================
        // instance data
        // ==================================================

        // required arguments:
        private Integer k           = null;
        private Integer n           = null;
        private BigInteger secret   = null;

        // optional: if 'secret' was given as a human-string, this is non-null
        // else this is null
        private String secretArgument = null;

        // optional:  if null, then do not use modulus
        // default to 384-bit
        
        private BigInteger modulus;
        //private BigInteger modulus = SecretShare.getPrimeUsedFor384bitSecretPayload();

        // optional description
        private String description = null;

        // optional: the random can be seeded
        private Random random;

        // if true, print on 1 sheet of paper; otherwise use 'n' sheets and repeat the header
        private boolean printAllSharesAtOnce = true;

        // ==================================================
        // constructors
        // ==================================================
        public static SplitInput parse(String[] args)
        {
            SplitInput ret = new SplitInput();

            boolean calculateModulus = false;
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
                }
                else if ("-n".equals(args[i]))
                {
                    i++;
                    ret.n = parseInt("n", args, i);
                }
                else if ("-d".equals(args[i]))
                {
                    i++;
                    checkIndex("d", args, i);
                    ret.description = args[i];
                }
                else if ("-sN".equals(args[i]))
                {
                    i++;
                    ret.secretArgument = null;
                    ret.secret = parseBigInteger("sN", args, i);
                    
                }
                else if ("-sS".equals(args[i]))
                {
                    i++;
                    ret.secretArgument = args[i];
                    ret.secret = BigIntUtilities.Human.createBigInteger(args[i]);
                    
                }
                else if ("-r".equals(args[i]))
                {
                    i++;
                    int seed =  parseInt("r", args, i);
                    ret.random = new Random(seed);
                }
                else if("-primeCustom".equals(args[i])){
                	ret.modulus = SecretShare.createModulus(ret.secret);
                	System.out.println("Prime: "+ ret.modulus);
                }
                else if ("-printOne".equals(args[i]))
                {
                    ret.printAllSharesAtOnce = true;
                }
                else if (args[i].startsWith("-printIndiv"))  // -printIndividual
                {
                    ret.printAllSharesAtOnce = false;
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
            checkRequired("-n", ret.n);
            checkRequired("-sN or -sS", ret.secret);

            if (calculateModulus)
            {
            	
            }
            
            if (ret.modulus != null)
            {
            	
                if (! SecretShare.isTheModulusAppropriateForSecret(ret.modulus, ret.secret))
                {
                	
                    final String originalString;
                    if (ret.secretArgument != null)
                    {
                        originalString = "[" + ret.secretArgument + "]";
                    }
                    else
                    {
                        originalString = "";
                    }

                    final String sInfo;
                    String sAsString = "" + ret.secret;
                    if (sAsString.length() < 25)
                    {
                        sInfo = sAsString;
                    }
                    else
                    {
                        sInfo = "length is " + sAsString.length() + " digits";
                    }
                    String m = "The secret " + originalString +  " (" + sInfo + ") is too big.  " +
                            "Please adjust the prime modulus or use -primeNone";

                    throw new SecretShareException(m);

                }
            }

            if (ret.random == null)
            {
                ret.random = new SecureRandom();
            }
            return ret;
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
        public SplitOutput output()
        {
            SplitOutput ret = new SplitOutput(this);
            ret.setPrintAllSharesAtOnce(printAllSharesAtOnce);
         
            SecretShare.PublicInfo publicInfo =
                new SecretShare.PublicInfo(this.n,
                                           this.k,
                                           this.modulus,
                                           this.description);

            SecretShare secretShare = new SecretShare(publicInfo);

            SecretShare.SplitSecretOutput generate = secretShare.split(this.secret, this.random);

            ret.splitSecretOutput = generate;

            return ret;
        }


        // ==================================================
        // non public methods
        // ==================================================
    }

    public static class SplitOutput
    {
        private static final String SPACES = "                                              ";
        private boolean printAllSharesAtOnce = true;

        private final SplitInput splitInput;
        private SplitSecretOutput splitSecretOutput;


        public SplitOutput(SplitInput inSplitInput)
        {
            this(true, inSplitInput);
        }

        public SplitOutput(boolean inPrintAllSharesAtOnce, SplitInput inSplitInput)
        {
            printAllSharesAtOnce = inPrintAllSharesAtOnce;
            splitInput = inSplitInput;
        }

        public void setPrintAllSharesAtOnce(boolean val)
        {
            printAllSharesAtOnce = val;
        }

        public void print(PrintStream out)
        {
            if (printAllSharesAtOnce)
            {
                
                printSharesAllAtOnce(out);
            }
            else
            {
                printSharesOnePerPage(out);
            }
        }
        
        public List<ShareInfo> retrieveShares(){
        	List<ShareInfo> shares = splitSecretOutput.getShareInfos();
        	
        	return shares;
        }
        public BigInteger getPrime(){
        	BigInteger prime = null;
        	prime = splitSecretOutput.getPublicInfo().getPrimeModulus();
        	return prime;
        }
        public int getThreshold(){
        	int threshold = 0;
        	threshold = splitSecretOutput.getPublicInfo().getK();
        	return threshold;
        }
        public int getNoOfShares(){
        	int noOfShares = 0;
        	noOfShares = splitSecretOutput.getPublicInfo().getN();
        	return noOfShares;
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




        private void printSharesOnePerPage(PrintStream out)
        {
            final List<SecretShare.ShareInfo> shares = splitSecretOutput.getShareInfos();
            boolean first = true;
            for (SecretShare.ShareInfo share : shares)
            {
                if (! first)
                {
                    printSeparatePage(out);
                }
                first = false;

                printHeaderInfo(out);



                printShare(out, share, false);
                printShare(out, share, true);

            }

        }

        private void printSeparatePage(PrintStream out)
        {
            out.print("\u000C");
        }

        private void printHeaderInfo(PrintStream out)
        {
            final SecretShare.PublicInfo publicInfo = splitSecretOutput.getPublicInfo();
            field(out, "Date", publicInfo.getDate());
            field(out, "UUID", publicInfo.getUuid());
            //field(out, "Description", publicInfo.getDescription());

            markedValue(out, "n", publicInfo.getN());
            markedValue(out, "k", publicInfo.getK());
            markedValue(out, "modulus", publicInfo.getPrimeModulus(), false);
            markedValue(out, "modulus(bigintcs)", publicInfo.getPrimeModulus(), true);
        }

        private void printSharesAllAtOnce(PrintStream out)
        {
            List<SecretShare.ShareInfo> shares = splitSecretOutput.getShareInfos();
            out.println("");
            for (SecretShare.ShareInfo share : shares)
            {
                printShare(out, share, false);
            }
            for (SecretShare.ShareInfo share : shares)
            {
                printShare(out, share, true);
            }
        }
        private void markedValue(PrintStream out,
                                 String fieldname,
                                 BigInteger number,
                                 boolean printAsBigIntCs)
        {
            String s;
            if (number != null)
            {
                if (printAsBigIntCs)
                {
                    s = BigIntUtilities.Checksum.createMd5CheckSumString(number);
                }
                else
                {
                    s = number.toString();
                }
                out.println(fieldname + " = " + s);
            }
            else
            {
                // no modulus supplied, do nothing
            }
        }
        private void markedValue(PrintStream out,
                                 String fieldname,
                                 int n)
        {
            out.println(fieldname + " = " + n);
        }


        private void field(PrintStream out,
                           String label,
                           String value)
        {
            if (value != null)
            {
                String sep;
                String pad;
                if ((label.length() > 0) &&
                    (! label.trim().equals("")))
                {
                    pad = label + SPACES;
                    pad = pad.substring(0, 30);
                    if (value.equals(""))
                    {
                        pad = label;
                        sep = "";
                    }
                    else
                    {
                        sep = ": ";
                    }
                }
                else
                {
                    pad = label;
                    sep = "";
                }

                out.println(pad + sep + value);
            }
        }

        private void printShare(PrintStream out,
                                ShareInfo share,
                                boolean printAsBigIntCs)
        {
            markedValue(out, "Share (x:" + share.getIndex() + ")", share.getShare(), printAsBigIntCs);
        }
    } // class SplitOutput

}
