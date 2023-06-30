/*Ex.No:12 Mini Project
Title:TEA Cipher
Team Members
204024:R.Mirunalini
204040:J.Rithi Afra
204052:N.SubaDharshini
*/
/*File Name:TEA.java*/
import java.util.Scanner;
import java.math.BigInteger; 
public class TEA
{
   private int _key[];  // The 128 bit key.
   private byte _keyBytes[];  // original key as found
   private int _padding;      // amount of padding added in byte --> integer conversion.

  
   public static void main(String args[])
   {
      byte key[] = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
      TEA t = new TEA(key);
      String src;
      Scanner S=new Scanner(System.in);
      System.out.println("Enter the string");
      src=S.next();
      System.out.println("input = " + src);
      byte plainSource[] = src.getBytes();
      int enc[] = t.encode(plainSource, plainSource.length);
      System.out.println("cipher text is"+enc);
      System.out.println(t.padding() + " bytes added as padding.");
      byte dec[] = t.decode(enc);
      System.out.println("Decryted text = " + new String(dec));
   }

  
   public TEA(byte key[])
   {
      int klen = key.length;
      _key = new int[4];
      
      // Incorrect key length throws exception.
      if (klen != 16)
         throw new ArrayIndexOutOfBoundsException(this.getClass().getName() + ": Key is not 16 bytes");

      int j, i;
      for (i = 0, j = 0; j < klen; j += 4, i++)
         _key[i] = (key[j] << 24 ) | (((key[j+1])&0xff) << 16) | (((key[j+2])&0xff) << 8) | ((key[j+3])&0xff);

      _keyBytes = key;  // save for toString.
   }

  
   public String toString()
   {
      String tea = this.getClass().getName();
      tea +=  ": Tiny Encryption Algorithm (TEA)  key: " + dumpBytes(_keyBytes);
      return tea;
   }

  
   
   public int [] encipher(int v[])
   {
      int y=v[0];
      int z=v[1];
      int sum=0;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      while(n-->0)
      {
         sum += delta;
         y += (z << 4)+a ^ z+sum ^ (z >> 5)+b;
         z += (y << 4)+c ^ y+sum ^ (y >> 5)+d;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }

   
   public int [] decipher(int v[])
   {
      int y=v[0];
      int z=v[1];
      int sum=0xC6EF3720;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      // sum = delta<<5, in general sum = delta * n 

      while(n-->0)
      {
         z -= (y << 4)+c ^ y+sum ^ (y >> 5) + d;
         y -= (z << 4)+a ^ z+sum ^ (z >> 5) + b;
         sum -= delta;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }

  
   public byte [] encipher(byte v[])
   {
      byte y=v[0];
      byte z=v[1];
      int sum=0;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      while(n-->0)
      {
         sum += delta;
         y += (z << 4)+a ^ z+sum ^ (z >> 5)+b;
         z += (y << 4)+c ^ y+sum ^ (y >> 5)+d;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }

   /**
   * Decipher two <code>bytess.
   *
   * @param v <code>byte array of 2
   *
   * @return decipherd <code>byte array of 2
   */
   public byte [] decipher(byte v[])
   {
      byte y=v[0];
      byte z=v[1];
      int sum=0xC6EF3720;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      // sum = delta<<5, in general sum = delta * n 

      while(n-->0)
      {
         z -= (y << 4)+c ^ y+sum ^ (y >> 5)+d;
         y -= (z << 4)+a ^ z+sum ^ (z >> 5)+b;
         sum -= delta;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }

  
   int [] encode(byte b[], int count)
   {
      int j ,i;
      int bLen = count;
      byte bp[] = b;

      _padding = bLen % 8;
      if (_padding != 0)   // Add some padding, if necessary.
      {
         _padding = 8 - (bLen % 8);
         bp = new byte[bLen + _padding];
         System.arraycopy(b, 0, bp, 0, bLen);
         bLen = bp.length;
      }

      int intCount = bLen / 4;
      int r[] = new int[2];
      int out[] = new int[intCount];

      for (i = 0, j = 0; j < bLen; j += 8, i += 2)
      {
         // Java's unforgivable lack of unsigneds causes more bit
         // twiddling than this language really needs.
         r[0] = (bp[j] << 24 ) | (((bp[j+1])&0xff) << 16) | (((bp[j+2])&0xff) << 8) | ((bp[j+3])&0xff);
         r[1] = (bp[j+4] << 24 ) | (((bp[j+5])&0xff) << 16) | (((bp[j+6])&0xff) << 8) | ((bp[j+7])&0xff);
         encipher(r);
         out[i] = r[0];
         out[i+1] = r[1];
      }

      return out;
   }

  
   public int padding()
   {
      return _padding;
   }

  
  
   public byte [] decode(byte b[], int count)
   {
      int i, j;

      int intCount = count / 4;
      int ini[] = new int[intCount];
      for (i = 0, j = 0; i < intCount; i += 2, j += 8)
      {
         ini[i] = (b[j] << 24 ) | (((b[j+1])&0xff) << 16) | (((b[j+2])&0xff) << 8) | ((b[j+3])&0xff);
         ini[i+1] = (b[j+4] << 24 ) | (((b[j+5])&0xff) << 16) | (((b[j+6])&0xff) << 8) | ((b[j+7])&0xff);
      }
      return decode(ini);
   }

 
   public byte [] decode(int b[])
   {
      // create the large number and start stripping ints out, two at a time.
      int intCount = b.length;

      byte outb[] = new byte[intCount * 4];
      int tmp[] = new int[2];

      // decipher all the ints.
      int i, j;
      for (j = 0, i = 0; i < intCount; i += 2, j += 8)
      {
         tmp[0] = b[i];
         tmp[1] = b[i+1];
         decipher(tmp);
         outb[j]   = (byte)(tmp[0] >>> 24);
         outb[j+1] = (byte)(tmp[0] >>> 16);
         outb[j+2] = (byte)(tmp[0] >>> 8);
         outb[j+3] = (byte)(tmp[0]);
         outb[j+4] = (byte)(tmp[1] >>> 24);
         outb[j+5] = (byte)(tmp[1] >>> 16);
         outb[j+6] = (byte)(tmp[1] >>> 8);
         outb[j+7] = (byte)(tmp[1]);
      }

      return outb;
   }


   
   private String dumpBytes(byte b[])
   {
      StringBuffer r = new StringBuffer();
      final String hex = "0123456789ABCDEF";

      for (int i = 0; i < b.length; i++)
      {
         int c = ((b[i]) >>> 4) & 0xf;
         r.append(hex.charAt(c));
         c = ((int)b[i] & 0xf);
         r.append(hex.charAt(c));
      }

      return r.toString();
   }



































































































































































}
/*Output
E:\>javac TEA.java

E:\>java TEA
Enter the string
Rithi
input = Rithi
cipher text is[I@1f96302
3 bytes added as padding.
Decryted text = Rithi
*/