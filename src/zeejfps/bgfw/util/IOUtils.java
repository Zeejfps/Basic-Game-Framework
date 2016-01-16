package zeejfps.bgfw.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.BufferUtils;

public class IOUtils {

	public static String resourceToString(String resource) throws IOException {
		StringBuilder buffer = new StringBuilder();
		Reader r;
		
		File file = new File(resource);
		if (file.isFile()) {
			r = new FileReader(file);
		}
		else {
			InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			r = new InputStreamReader(fis);
		}
		
		BufferedReader br = new BufferedReader(r);
		try {
			String line;	
			while ((line = br.readLine()) != null) {
				buffer.append(line).append("\n");
			}
		}
		finally {
			br.close();
		}
		
		return buffer.toString();
	}
	
	public static ByteBuffer resourceToBuffer(String resource) throws IOException {
		ByteBuffer buffer;

		File file = new File(resource);
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			
			buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);

			while (fc.read(buffer) != -1);
			
			fis.close();
			fc.close();
		} 
		else {
			InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (fis == null)
				throw new FileNotFoundException(resource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();	
			try {
				int b;
				while ((b = fis.read()) != -1) {
					baos.write(b);
				}
				byte[] bytes = baos.toByteArray();

				buffer = BufferUtils.createByteBuffer(bytes.length);
				buffer.put(bytes);	
			} finally {
				fis.close();
				baos.close();
			}
		}

		buffer.flip();
		return buffer;
	}
}
