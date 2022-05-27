import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
public class Main {

	public static void main(String[] args) throws IOException{
		Scanner terminalInput = new Scanner(System.in);
        String pilihanUser;
        boolean isLanjutkan = true;

        while (isLanjutkan) {
            System.out.println("Database Pudding\n");
            System.out.println("1. Insert Menu");
            System.out.println("2. View Menu");
            System.out.println("3. Update Menu");
            System.out.println("4. Delete Menu");
            System.out.println("5. Exit");

            System.out.print("\n\nPilihan anda: ");
            pilihanUser = terminalInput.next();

            switch (pilihanUser) {
                case "1":
                   
                    System.out.println("\n================");
                    System.out.println("Insert Menu");
                    System.out.println("================");
                    tambahData();
                    break;
                case "2":
                    System.out.println("\n=================");
                    System.out.println("View Menu");
                    System.out.println("=================");
                    tampilkanMenu();
                    break;
                case "3":
                    
                    System.out.println("\n==============");
                    System.out.println("Update Menu");
                    System.out.println("==============");
                    updateData();
                    break;
                case "4":
                    
                    System.out.println("\n===============");
                    System.out.println("Delete Menu");
                    System.out.println("===============");
                    deleteData();
                    break;
                case "5":
                	System.exit(0);
                    break;
                default:
                    System.err.println("\nInput anda tidak ditemukan\nSilahkan pilih [1-5]");
            }

            isLanjutkan = getYesorNo("Apakah Anda ingin melanjutkan");
        }
    }
	
	private static void tampilkanMenu() throws IOException{
		        FileReader fileInput;
		        BufferedReader bufferInput;

		        try {
		            fileInput = new FileReader("database");
		            bufferInput = new BufferedReader(fileInput);
		        } catch (Exception e){
		            System.err.println("Database Tidak ditemukan");
		            System.err.println("Silahkan tambah data");
		            return;
		        }


		        System.out.println("\n| No |\tKode\t |\tNama                |\tHarga               |\tStok");
		        System.out.println("----------------------------------------------------------------------------------------------------------");

		        String data = bufferInput.readLine();
		        int nomorData = 0;
		        while(data != null) {
		            nomorData++;

		            StringTokenizer stringToken = new StringTokenizer(data, ",");

		            stringToken.nextToken();
		            System.out.printf("| %2d ", nomorData);
		            System.out.printf("|\t%-7s  ", stringToken.nextToken());
		            System.out.printf("|\t%-17s   ", stringToken.nextToken());
		            System.out.printf("|\t%-17s   ", stringToken.nextToken());
		            System.out.printf("|\t%s   ", stringToken.nextToken());
		            System.out.print("\n");

		            data = bufferInput.readLine();
		        }

		        System.out.println("----------------------------------------------------------------------------------------------------------");
		    }
	
	private static boolean getYesorNo(String message){
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n)? ");
        String pilihanUser = terminalInput.next();

        while(!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")) {
            System.err.println("Pilihan anda bukan y atau n");
            System.out.print("\n"+message+" (y/n)? ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");

    }
	
	private static void tambahData() throws IOException{


        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);


        // mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String kode, nama, harga, stok;

        System.out.print("masukan kode: ");
        kode = terminalInput.nextLine();
        System.out.print("masukan nama menu: ");
        nama = terminalInput.nextLine();
        System.out.print("masukan harga menu: ");
        harga = terminalInput.nextLine();
        System.out.print("masukan stok menu: ");
        stok = terminalInput.nextLine();

        String[] keywords = {kode+","+nama+","+harga+","+stok};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = cekMenu(keywords,false);

        if (!isExist){
            System.out.println(ambilKode(nama, kode));
            long nomorEntry = ambilKode(nama, kode) + 1;

            String nama1 = nama.replaceAll("\\s+","");
            String primaryKey = nama1+"_"+kode+"_"+nomorEntry;
            System.out.println("\nData yang akan anda masukan adalah");
            System.out.println("----------------------------------------");
            System.out.println("primary key  : " + primaryKey);
            System.out.println("tahun terbit : " + kode);
            System.out.println("penulis      : " + nama);
            System.out.println("judul        : " + harga);
            System.out.println("penerbit     : " + stok);

            boolean isTambah = getYesorNo("Apakah akan ingin menambah data tersebut? ");

            if(isTambah){
                bufferOutput.write(primaryKey + "," + kode + ","+ nama +"," + harga + ","+kode);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("Data yang anda akan masukan sudah tersedia di database dengan data berikut:");
            cekMenu(keywords,true);
        }


        bufferOutput.close();
    }
	
	private static long ambilKode(String nama, String kode) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            nama = nama.replaceAll("\\s+","");

            if (nama.equalsIgnoreCase(dataScanner.next()) && kode.equalsIgnoreCase(dataScanner.next()) ) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        return entry;
    }
	
	 
	 private static boolean cekMenu(String[] keywords, boolean isDisplay) throws IOException{

	        FileReader fileInput = new FileReader("database.txt");
	        BufferedReader bufferInput = new BufferedReader(fileInput);

	        String data = bufferInput.readLine();
	        boolean isExist = false;
	        int nomorData = 0;

	        if (isDisplay) {
	            System.out.println("\n| No |\tKode |\tNama                |\tHarga               |\tStok");
	            System.out.println("----------------------------------------------------------------------------------------------------------");
	        }

	        while(data != null){

	            isExist = true;

	            for(String keyword:keywords){
	                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
	            }


	            if(isExist){
	                if(isDisplay) {
	                    nomorData++;
	                    StringTokenizer stringToken = new StringTokenizer(data, ",");

	                    stringToken.nextToken();
			            System.out.printf("| %2d ", nomorData);
			            System.out.printf("|\t%-7s  ", stringToken.nextToken());
			            System.out.printf("|\t%-17s   ", stringToken.nextToken());
			            System.out.printf("|\t%-17s   ", stringToken.nextToken());
			            System.out.printf("|\t%s   ", stringToken.nextToken());
	                    System.out.print("\n");
	                } else {
	                    break;
	                }
	            }

	            data = bufferInput.readLine();
	        }

	        if (isDisplay){
	            System.out.println("----------------------------------------------------------------------------------------------------------");
	        }

	        return isExist;
	    }
	 
	 private static void deleteData() throws  IOException{
	        File database = new File("database.txt");
	        FileReader fileInput = new FileReader(database);
	        BufferedReader bufferedInput = new BufferedReader(fileInput);

	        File tempDB = new File("tempDB.txt");
	        FileWriter fileOutput = new FileWriter(tempDB);
	        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

	        System.out.println("List Menu");
	        tampilkanMenu();

	        Scanner terminalInput = new Scanner(System.in);
	        System.out.print("\nMasukan nomor Menu yang akan dihapus: ");
	        int deleteNum = terminalInput.nextInt();


	        boolean isFound = false;
	        int entryCounts = 0;

	        String data = bufferedInput.readLine();

	        while (data != null){
	            entryCounts++;
	            boolean isDelete = false;

	            StringTokenizer st = new StringTokenizer(data,",");

	            if (deleteNum == entryCounts){
	                System.out.println("\nData yang ingin anda hapus adalah:");
	                System.out.println("-----------------------------------");
	                System.out.println("Kode       : " + st.nextToken());
	                System.out.println("Nama           : " + st.nextToken());
	                System.out.println("Harga         : " + st.nextToken());
	                System.out.println("Stok        : " + st.nextToken());

	                isDelete = getYesorNo("Apakah anda yakin akan menghapus?");
	                isFound = true;
	            }

	            if(isDelete){
	                System.out.println("Data berhasil dihapus");
	            } else {
	                bufferedOutput.write(data);
	                bufferedOutput.newLine();
	            }
	            data = bufferedInput.readLine();
	        }

	        if(!isFound){
	            System.err.println("Menu tidak ditemukan");
	        }

	        bufferedOutput.flush();
	        database.delete();
	        tempDB.renameTo(database);

	    }
	 
	 private static void updateData() throws IOException{
	        File database = new File("database.txt");
	        FileReader fileInput = new FileReader(database);
	        BufferedReader bufferedInput = new BufferedReader(fileInput);

	        File tempDB = new File("tempDB.txt");
	        FileWriter fileOutput = new FileWriter(tempDB);
	        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

	        System.out.println("List Menu");
	        tampilkanMenu();

	        Scanner terminalInput = new Scanner(System.in);
	        System.out.print("\nMasukan nomor buku yang akan diupdate: ");
	        int updateNum = terminalInput.nextInt();


	        String data = bufferedInput.readLine();
	        int entryCounts = 0;

	        while (data != null){
	            entryCounts++;

	            StringTokenizer st = new StringTokenizer(data,",");

	            if (updateNum == entryCounts){
	                System.out.println("\nData yang ingin anda update adalah:");
	                System.out.println("---------------------------------------");
	                System.out.println("Kode               : " + st.nextToken());
	                System.out.println("Nama             : " + st.nextToken());
	                System.out.println("Harga            : " + st.nextToken());
	                System.out.println("Stok               : " + st.nextToken());



	                String[] fieldData = {"kode","nama","harga","stok"};
	                String[] tempData = new String[4];

	                st = new StringTokenizer(data,",");
	                String originalData = st.nextToken();


	                st = new StringTokenizer(data,",");
	                st.nextToken();
	                System.out.println("\nData baru anda adalah ");
	                System.out.println("---------------------------------------");
	                System.out.println("Kode               : " + st.nextToken() + " --> " + tempData[0]);
	                System.out.println("Nama             : " + st.nextToken() + " --> " + tempData[1]);
	                System.out.println("Harga            : " + st.nextToken() + " --> " + tempData[2]);
	                System.out.println("Stok               : " + st.nextToken() + " --> " + tempData[3]);


	                boolean isUpdate = getYesorNo("apakah anda yakin ingin mengupdate data tersebut");

	                if (isUpdate){

	                    boolean isExist = cekMenu(tempData,false);

	                    if(isExist){
	                        System.err.println("Menu sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
	                        bufferedOutput.write(data);

	                    } else {

	                        String kode = tempData[0];
	                        String nama = tempData[1];
	                        String harga = tempData[2];
	                        String stok = tempData[3];

	                        long nomorEntry = ambilKode(nama, kode) + 1;

	                        String nama1 = nama.replaceAll("\\s+","");
	                        String primaryKey = nama1+"_"+kode+"_"+nomorEntry;

	                        bufferedOutput.write(primaryKey + "," + kode + ","+ nama +"," + harga + ","+stok);
	                    }
	                } else {
	                    bufferedOutput.write(data);
	                }
	            } else {
	                bufferedOutput.write(data);
	            }
	            bufferedOutput.newLine();

	            data = bufferedInput.readLine();
	        }

	        bufferedOutput.flush();

	        database.delete();
	        tempDB.renameTo(database);

	    }


	}
