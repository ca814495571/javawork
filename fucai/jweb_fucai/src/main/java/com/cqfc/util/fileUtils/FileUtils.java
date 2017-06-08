package com.cqfc.util.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cqfc.management.dao.UserInfoOfFTPFileDao;
import com.cqfc.management.model.UserInfoOfFTPFile;

public class FileUtils {

	// 文件列表为全局变量，否则递归算法会导致读取文件列表失败，大型文件夹速度慢
	List<File> fileList = new ArrayList<File>();

	/**
	 * 读取单个文件内容

	 * @param path
	 */
	public void readFile(File file) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			// BufferedReader br = new BufferedReader(new InputStreamReader(new
			// FileInputStream(new File(path))));
//
			String str = "";
			
			while ((str = br.readLine())!= null) {
					
				System.out.println(str);
				
				// 封装数据
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		
	}

	/**
	 * 获取指定目录下的所有文件列表
	 * 
	 * @param file
	 * @return
	 */
	public List<File> getFileLists(File file) {

		if (!file.exists() || file == null) {
			System.out.println(file.getPath() + "文件不存在");
			return null;
		}
		if (file.isFile()) {

			fileList.add(file);
		} else {

			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {

				File childFile = files[i];
				if (childFile.isFile()) {

					fileList.add(childFile);
				} else {

					getFileLists(childFile);
				}
			}

		}
		return fileList;

	}

	/**
	 * 拷贝目录下所有文件到另一个目录下
	 *
	 */
	public boolean copyFileLists(File file, String pathName) {

		if (!file.exists() || file == null) {
			System.out.println(file.getPath() + "文件不存在");
			return false;
		}

		if ("".equals(pathName)) {
			return false;
		}

		File destFile = new File(pathName);

		if (!destFile.exists()) {
			destFile.mkdir();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String destFileName = "";
		try {
			if (file.isFile()) {

				destFileName = pathName + File.separator + file.getName();
				fis = new FileInputStream(file);
				fos = new FileOutputStream(new File(destFileName));
				writeToFile(fis, fos);
			} else {

				// 添加新的文件条目
				destFileName = pathName + File.separator + file.getName();
				File fileEntry = new File(destFileName);
				fileEntry.mkdir();
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {

					if (files[i].isFile()) {

						File childFile = new File(destFileName + File.separator
								+ files[i].getName());
						// 添加新的子文件
						childFile.createNewFile();
						fis = new FileInputStream(files[i]);
						fos = new FileOutputStream(childFile);
						writeToFile(fis, fos);
					} else {

						copyFileLists(files[i], destFileName);
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 删除文件下面所有文件以及文件目录
	 * 
	 * @param file
	 * @return
	 */

	public boolean deleteFileLists(File file) {

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isFile()) {

			file.delete();
		} else {

			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {

				if (files[i].isFile()) {
					files[i].delete();

				} else {
					deleteFileLists(files[i]);
				}
			}
		}
		file.delete();
		return true;

	}

	public void writeToFile(FileInputStream fis, FileOutputStream fos) {
		byte[] b = new byte[1024];
		int len = 0;
		try {

			while ((len = fis.read(b)) != -1) {

				fos.write(b, 0, len);
			}
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		FileUtils fl = new FileUtils();

		fl.readFile(new File("d:/planinfo2014052920.txt"));
		
		/*
		 * List<File> fileList = fl.getFileLists(new File("E:/youdao"));
		 * 
		 * for (int i = 0; i < fileList.size(); i++) {
		 * 
		 * System.out.println(fileList.get(i).getPath()); }
		 */

		// System.out.println(fl.copyFileLists(new File("E:/youdao"),
		// "F:/HEHE"));

		//System.out.println(fl.deleteFileLists(new File("F:/HEHE")));

	}

}
