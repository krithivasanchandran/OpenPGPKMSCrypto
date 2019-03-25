package com.paypal.custom.integrity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IntegrityChecker {

	public boolean checkFileIntegritry(String initialFile, String outputFile) throws Exception {

		if (initialFile != null && !(initialFile.isEmpty())) {
			if (outputFile != null && !(outputFile.isEmpty())) {

				try {

					/*
					 * Reading Initial File Contents
					 */

					String inputFile = readFileContents(initialFile);
					
					if(inputFile == null) { 
						System.out.println("Please make sure the input File has contents");
						throw new Exception("The inputFile to be submitted for Encryption is empty");
					}
					
					/*
					 * Reading Decrypted File Contents
					 */

					String decryptedFile = readFileContents(outputFile);
					
					if(decryptedFile == null){
						System.out.println("The decrypted File contents is empty or null");
						throw new Exception("The decrypted File seems to be empty");
					}

					int inLen = inputFile.length();
					int outLen = decryptedFile.length();
					boolean check = false;

					if (inLen == outLen) {
						System.out.println("The length of the two Files are same ");
						int i = 0;

						while (i < inLen) {
							if (inputFile.charAt(i) == decryptedFile.charAt(i)) {
								i++;
								continue;
							} else {
								check = true;
								System.out.println("The file contents are not equal");
								break;
							}
						}
					}

					if (check) {
						return false;
					}

				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

			}
		}
		return true;
	}

	private String readFileContents(String fileName) throws IOException {

		BufferedReader reader = null;

		try {
			File file = new File(fileName);
			reader = new BufferedReader(new FileReader(file));

			StringBuilder builder = new StringBuilder();

			String str = null;

			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}

			return builder.toString();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return null;
	}

}
