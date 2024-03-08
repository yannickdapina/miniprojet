import java.io.*;
import java.util.zip.*;

public class senfilecompressor {
    public static void main(String[] args) {
        if (args.length < 1 || args[0].equals("-h")) {
            printHelp();
            return;
        }
        
        String option = args[0];
        switch (option) {
            case "-c":
                compressFiles(args);
                break;
            case "-d":
                decompressFile(args);
                break;
            default:
                System.out.println("Invalid option. Use -h for help.");
        }
    }
    
    private static void compressFiles(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("compressed.sfc");
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            for (int i = 1; i < args.length; i++) {
                File file = new File(args[i]);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }
                
                fis.close();
            }
            
            zos.close();
            fos.close();
            
            System.out.println("Compression successful. Output file: compressed.sfc");
        } catch (IOException e) {
            System.out.println("Compression failed: " + e.getMessage());
        }
    }
    
    private static void decompressFile(String[] args) {
        try {
            String zipFilePath = args[1];
            String outputDirectory = ".";
            if (args.length > 2 && args[2].equals("-r")) {
                outputDirectory = args[3];
            }
            
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();
            
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(outputDirectory + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                
                FileOutputStream fos = new FileOutputStream(newFile);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = zis.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
                
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            
            zis.closeEntry();
            zis.close();
            fis.close();
            
            System.out.println("Decompression successful.");
        } catch (IOException e) {
            System.out.println("Decompression failed: " + e.getMessage());
        }
    }
    
    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("java SenFileCompressor -c <files to compress...>");
        System.out.println("java SenFileCompressor -d <compressed file>");
    }
}

