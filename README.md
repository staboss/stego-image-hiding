# Hiding data in images using steganography techniques
The Kotlin program, based on steganography methods, for hiding information in images using the Least Significant Bit (LSB) and Discrete Cosine Transform (DCT) methods.

## Requirements
- [JAVA 8+](https://www.java.com/en/download/)
- [GRADLE](https://docs.gradle.org/current/userguide/installation.html#installing_with_a_package_manager)

## Build project
    ➜  stego-image-hiding: gradle build
    ➜  ...
    ➜  stego-image-hiding: gradle jar

## Usage 

    usage: java -jar stego-image-hiding.jar -e|-d -m METHOD -s IMAGE [-r IMAGE] [-t TEXT_FILE] [-k KEY_FILE]

```
optional arguments:
  -d           : extract message
  -e           : embed message
  -s IMAGE     : source image
  -r IMAGE     : result image
  -m METHOD    : DCT or LSB
  -k KEY_FILE  : binary key file [required for DCT]
  -t TEXT_FILE : secret message
```

## Examples
  #### DCT
  - Embedding
    ```
    ➜  java -jar stego-image-hiding.jar -e -m DCT -s src.bmp -r res.bmp -t text.txt -k key.txt
    ```
  - Extracting
    ```
    ➜  java -jar stego-image-hiding.jar -d -m DCT -s res.bmp -k key.txt
    ```
  #### LSB
  - Embedding
    ```
    ➜  java -jar stego-image-hiding.jar -e -m LSB -s src.bmp -r res.bmp -t text.txt
    ```
  - Extracting
    ```
    ➜  java -jar stego-image-hiding.jar -d -m LSB -s res.bmp
    ```

## License & copyright
Licensed under the [MIT-License](LICENSE.md).