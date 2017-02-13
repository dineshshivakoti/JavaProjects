package des_Implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FeistelCipher {
	// initial permuation table
	private static int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36,
			28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32,
			24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19,
			11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
	// inverse initial permutation
	private static int[] I_IP = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15,
			55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53,
			21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19,
			59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
	// Permutation P (in f(Feistel) function)
	private static int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5,
			18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4,
			25

	};
	// initial key permutation 64 => 56 biti
	private static int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
			26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63,
			55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53,
			45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
	// key permutation at round i 56 => 48
	private static int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
			23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55,
			30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29,
			32 };

	// expansion permutation from function f
	private static int[] expan_Table = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
			8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20,
			21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31,
			32, 1 };
	// substitution boxes
	private static final byte[][] sboxes = {
			{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7,
					4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8,
					13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4,
					9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 },
			{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4,
					7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11,
					10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
					15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 },
			{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0,
					9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8,
					15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
					8, 7, 4, 15, 14, 3, 11, 5, 2, 12 },
			{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11,
					5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12,
					11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
					13, 8, 9, 4, 5, 11, 12, 7, 2, 14 },
			{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2,
					12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10,
					13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14,
					2, 13, 6, 15, 0, 9, 10, 4, 5, 3 },
			{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4,
					2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2,
					8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15,
					10, 11, 14, 1, 7, 6, 0, 8, 13 },
			{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11,
					7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13,
					12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
					10, 7, 9, 5, 0, 15, 14, 2, 3, 12 },
			{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13,
					8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9,
					12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
					8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
	// holds subkeys
	private static byte[][] K;
	
	public String txt;
	public static void main(String args[]) throws IOException {
		String txt = null;
		String cipher_txt=null;
		byte[] input_bytes = new byte[1];
		byte[] key_bytes = new byte[1];
		boolean Is_key_size64 = false;

		System.out.print("Text Input for Encription :");
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			txt = br.readLine();
			input_bytes = txt.getBytes();
			do {
				System.out.println("Give a 64 bit key :");
				String input_key = br.readLine();
				if (input_key.length() == 8) {
					Is_key_size64 = true;
					key_bytes = input_key.getBytes();
				}

			} while (!Is_key_size64);
		} catch (Exception e) {
			System.out.println("Error occured:" + e.getMessage());
		}

		pauseProg();
		System.out.println("Encrypting-------- ");
		byte[] encrypted = encrypt(input_bytes, key_bytes);
		System.out.println("cipher text:" + encrypted);
		System.out.println("\n\n Encryption  Successfull");
		System.out.println("Press enter to start Decryption");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		cipher_txt = br.readLine();
		System.out.println("Enter cipher text :");
		String cipher_text = br.readLine();
		System.out.println("\n\nDecrypting-------------");
		byte[] decrypted = decrypt(encrypted, key_bytes);
		String D = new String(decrypted);
		
		System.out.print("Decrypted Text :" + D);
		System.out.print("\n\n Ecrypted Text :" + txt);
		System.out.println(" \n\n Decryption Successfull ");

	}

	//Dealing with bytes is more easy in Java then with bits. Byte requires additional functions to seperate and set the bits
	//bit_Set function finds the byte place and bit place within that byte  which is to be set.

	private static void bit_Set(byte[] input, int place, int val) {
		int byte_place = place / 8;
		int bit_place = place % 8;
		byte tmpB = input[byte_place];
		tmpB = (byte) (((0xFF7F >> bit_place) & tmpB) & 0x00FF);
		byte newByte = (byte) ((val << (8 - (bit_place + 1))) | tmpB);
		input[byte_place] = newByte;
	}

	// get_bit gets the bit from the given position.

	private static int get_bit(byte[] input, int place) {
		int byte_place = place / 8;
		int bit_place = place % 8;
		byte tmpB = input[byte_place];
		int bit = tmpB >> (8 - (bit_place + 1)) & 0x0001;
		return bit;
	}

	// given an input vector of bytes -rotate2Right rotate bits to the right by
	// 2 positions.
	private static byte[] rotate2Right(byte[] input, int num_bits) {

		int rotTwice = 2;
		int nrBytes = (num_bits - 1) / 8 + 1;
		byte[] out = new byte[nrBytes];
		for (int i = 0; i < num_bits; i++) {
			int val = get_bit(input, i);
			bit_Set(out, (i + rotTwice) % num_bits, val);// shift 2 right
		}
		return out;
	}

	// get_bits  gets a series of consecutive bits from a starting position and
	// a given length.
	private static byte[] get_bits(byte[] input, int place, int n) {
		int numOfBytes = (n - 1) / 8 + 1;
		byte[] out = new byte[numOfBytes];
		for (int i = 0; i < n; i++) {
			int val = get_bit(input, place + i);
			bit_Set(out, i, val);
		}
		return out;

	}
	// XOR apply xor function on two vector of bytes.Simple xor function on two
	// byte arrays
	private static byte[] XOR(byte[] a, byte[] b) {

		byte[] xor_result = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			xor_result[i] = (byte) (a[i] ^ b[i]);
		}
		return xor_result;

	}
	
	// Function permutFunc  is a generalized function for applying a
		// permutation on a vector of bytes.
		private static byte[] permutation_Func(byte[] input, int[] table) {
			int nrBytes = (table.length - 1) / 8 + 1;
			byte[] out = new byte[nrBytes];
			for (int i = 0; i < table.length; i++) {
				int val = get_bit(input, table[i] - 1);
				bit_Set(out, i, val);
			}
			return out;

		}

	// This function is used for both encryption/decryption.
	// It takes fixed length block of 64 bits of data and the subkeys from the
	// key schedule.
	private static byte[] Block_encrypt(byte[] bloc, byte[][] subkeys,
			boolean isDecrypt) {
		byte[] tmp = new byte[bloc.length];
		byte[] R = new byte[bloc.length / 2];
		byte[] L = new byte[bloc.length / 2];

		tmp = permutation_Func(bloc, IP);

		L = get_bits(tmp, 0, 32);
		R = get_bits(tmp, 32, 32);

		// 16 rounds will start here
		for (int n = 0; n < 16; n++) {
			System.out.println("\n-------------");
			// System.out.println("Round " + (i+1) + ":");
			// newR is the new R half generated by the Fiestel function. If it
			// is encrpytion then the KS method is called to generate the
			// subkey otherwise the stored subkeys are used in reverse order
			// for decryption.
			byte[] newR = R;
			if (isDecrypt) {
				R = Feistel(R, subkeys[15 - n]);
				printBytes(L, "L", 0);
				printBytes(L, "R", 0);
				printBytes(subkeys[0], "K", 0);

			} else
				R = Feistel(R, subkeys[n]);
			//xor-ing the L and new R gives the new L value. new L is stored
			// in R and new R is stored in L, thus exchanging R and L for the
			// next round.
			R = XOR(L, R);
			L = newR;
			printBytes(L, "L", n + 1);
			printBytes(R, "R", n + 1);
			printBytes(subkeys[n], "K", n + 1);
		}

		tmp = merge_Bits(R, 32, L, 32);

		tmp = permutation_Func(tmp, I_IP);
		return tmp;
	}

	private static void printBytes(byte[] data, String name, int round) {

		System.out.println("");
		System.out.print(name + "" + round + ":");
		for (int i = 0; i < data.length; i++) {
			System.out.print(byteToBits(data[i]) + " ");
		}
		System.out.println();
	}

	private static String byteToBits(byte b) {

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 8; i++)
			buf.append((int) (b >> (8 - (i + 1)) & 0x0001));
		return buf.toString();
	}

	// Function Feistel  is the Feistel function.
	private static byte[] Feistel(byte[] R, byte[] K) {
		// First the 32 bits of the R array are expanded using expan_Table table.
		byte[] tmp;
		tmp = permutation_Func(R, expan_Table);
		// We xor the expanded R and the generated round key
		tmp = XOR(tmp, K);
		// The S boxes are then applied to this xor result and this is the
				// output of the Fiestel function.
		tmp = sboxes_func(tmp);
		// Second permutation is done here
		tmp = permutation_Func(tmp, P);
		return tmp;
	}

	// sboxes_func applies the 8 s-boxes to the 48bits of data resulting 32 bits
	// of output. The 48 bits are divided in groups of 6 bits. First and
	// last bit of each group will indicate a row in s-box.Middle 4 will
	// indicate a column. corresponding
	// value of 4 bits from the s-box will substitute the input of 6 bits.
	private static byte[] sboxes_func(byte[] in) {
		in = separateBytes(in, 6);
		byte[] out = new byte[in.length / 2];
		int halfByte = 0;
		for (int b = 0; b < in.length; b++) {
			byte valByte = in[b];
			int r = 2 * (valByte >> 7 & 0x0001) + (valByte >> 2 & 0x0001);
			int val = sboxes[b][r];
			if (b % 2 == 0)
				halfByte = val;
			else
				out[b / 2] = (byte) (16 * halfByte + val);
		}
		return out;
	}

	private static byte[] separateBytes(byte[] in, int len) {
		int numOfBytes = (8 * in.length - 1) / len + 1;
		byte[] out = new byte[numOfBytes];
		for (int i = 0; i < numOfBytes; i++) {
			for (int j = 0; j < len; j++) {
				int val = get_bit(in, len * i + j);
				bit_Set(out, 8 * i + j, val);
			}
		}
		return out;
	}

	private static byte[] merge_Bits(byte[] a, int aLen, byte[] b, int bLen) {
		int numOfBytes = (aLen + bLen - 1) / 8 + 1;
		byte[] out = new byte[numOfBytes];
		int j = 0;
		for (int i = 0; i < aLen; i++) {
			int val = get_bit(a, i);
			bit_Set(out, j, val);
			j++;
		}
		for (int i = 0; i < bLen; i++) {
			int val = get_bit(b, i);
			bit_Set(out, j, val);
			j++;
		}
		return out;
	}

	private static byte[] deletePadding(byte[] input) {
		int count = 0;

		int i = input.length - 1;
		while (input[i] == 0) {
			count++;
			i--;
		}

		byte[] tmp = new byte[input.length - count - 1];
		System.arraycopy(input, 0, tmp, 0, tmp.length);
		return tmp;
	}

	// The Key Structure function generates the round keys. C and D are the new
	// values of C and D which will be generated in this round.
	private static byte[][] subKeysgenerator(byte[] key) {

		byte[][] tmp = new byte[16][];
		// First Permutation is done here, 64 bits >>> 56 bits
		byte[] tmpK = permutation_Func(key, PC1);

		byte[] C = get_bits(tmpK, 0, PC1.length / 2);
		byte[] D = get_bits(tmpK, PC1.length / 2, PC1.length / 2);

		for (int i = 0; i < 16; i++) {
			// cd stores the combined C and D halves
			C = rotate2Right(C, 28);
			D = rotate2Right(D, 28);
			byte[] cd = merge_Bits(C, 28, D, 28);

			// Second Permutation is done here.
			tmp[i] = permutation_Func(cd, PC2);
		}

		return tmp;
	}

	public static byte[] encrypt(byte[] data, byte[] key) {
		int lenght = 0;
		byte[] padding = new byte[1];
		int i;
		lenght = 8 - data.length % 8;
		padding = new byte[lenght];
		padding[0] = (byte) 0x80;

		for (i = 1; i < lenght; i++)
			padding[i] = 0;

		byte[] tmp = new byte[data.length + lenght];
		byte[] bloc = new byte[8];

		K = subKeysgenerator(key);

		int count = 0;

		for (i = 0; i < data.length + lenght; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = Block_encrypt(bloc, K, false);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
			else {
				bloc[i % 8] = padding[count % 8];
				count++;
			}
		}
		if (bloc.length == 8) {
			bloc = Block_encrypt(bloc, K, false);
			System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
		}
		return tmp;
	}

	public static byte[] decrypt(byte[] data, byte[] key) {
		int i;
		byte[] tmp = new byte[data.length];
		byte[] bloc = new byte[8];

		K = subKeysgenerator(key);

		for (i = 0; i < data.length; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = Block_encrypt(bloc, K, true);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
		}
		bloc = Block_encrypt(bloc, K, true);
		System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);

		tmp = deletePadding(tmp);

		return tmp;
	}

	public static void pauseProg() {
		System.out.println("Press enter to  Encrypt");
		Scanner keyboard = new Scanner(System.in);
		keyboard.nextLine();
	}
	
}