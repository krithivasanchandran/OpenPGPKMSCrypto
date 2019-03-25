# OpenPGPKMSCrypto
Text file encryption and decryption using bouncy castle Library

## Legacy 

Traditionally , in legacy application the secret keys were present in the protected packages folder in local application environment only. Google Key Maker re-defined App Security bringing in tighter security guidelines to secure the way application communicate each other by encrypting the files (text or pdf or word or jpeg etc.) and send it over the network .

### More Info : https://cloud.google.com/kms/

## Objective : 

Application to improve the security of files transmitted over the network has to be at the highest security standards. Bouncy Castle Libraries is used by Symmetric , Asymmetric, PGP Encryption internally by the frameworks.

## Environment : 

- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Standalone Java 1.8+ Application with libraries (jdk or jre) `

- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Apache HTTP Client 4.5+`

- ![#1589F0](https://placehold.it/15/1589F0/000000?text=+) `Bouncy Castle PGP Encryption https://www.bouncycastle.org/latest_releases.html`

- ![#1589F0](https://placehold.it/15/1589F0/000000?text=+) `Jackson - Json libarary`

- ![#c5f015](https://placehold.it/15/c5f015/000000?text=+)  `Google KMS Client Library`

## Application Input Arguments :

- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Application Name - args[0]`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `KeyName - Key_trial_1, Key_trial_2, Key_trial_3 - args[1]`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Operation Name - ENC => Encryption , DEC => Decryption - args[2]`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Absolute Path File Name - eg. C:/Users/sampleDir/file_sample_input.txt - for the encryption/decryption file utility. - args[3]`

# Architecture

![alt text](https://github.com/krithivasanchandran/OpenPGPKMSCrypto/blob/master/architecture.png)

## Application Business Flow : 

Get the input from the User Interface or args[] - 
App name, Key name , Operation Name (Encryption (ENC) or Decryption (DEC)), Path of File Name for encryption.
Do the validation checks for the user inputs.
Generate the JWT Token ID from the Google KMS keys
Get the response back from the Google KMS agent - PGP key which is BASE64 encoded from the response JSON of the Keymaker API. 
Decrypt the base64 keymaker response keys and use bouncy castle to encrypt the file using OpenPGP Encryption. 
Test the scenarios for lossless data encryption and decryption . 

## How to Run

- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `git clone "urlPath"`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Import into eclipse or Intellij workspace`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Run mvn clean install`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Target Folder should be generated`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Right Click on Project Folder -> Run As -> Server -> Tomcat 8`
- ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Navigate to browser http://localhost:8080/GoogleKMS`




