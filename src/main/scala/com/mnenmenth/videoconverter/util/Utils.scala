package com.mnenmenth.videoconverter.util

import java.io._
import java.nio.channels.Channels

import com.mnenmenth.videoconverter.ui.Output

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object Utils {

  private class StdReader(is: InputStream, output: Output = null) extends Thread {
    override def run(): Unit = {
      val isr = new InputStreamReader(is)
      val br = new BufferedReader(isr)
      /*var line = ""
      while ((line = br.readLine()) != null) {
        if (output != null) {
          output.text.text += line + "\n"
          output.verticalScrollBar.value = output.verticalScrollBar.maximum
        } else {
          if (line != null) println(line)
        }
      }*/
      Stream.continually(br.readLine()).takeWhile(_ != null).foreach {line=>
        if (output != null) {
          output.text.text += line + "\n"
          output.verticalScrollBar.value = output.verticalScrollBar.maximum
        } else {
          if (line != null) println(line)
        }
      }

      if (output != null) {
        output.text.text += "Conversion Finished\n"
        output.verticalScrollBar.value = output.verticalScrollBar.maximum
      } else
        println("Conversion Finished")

    }
  }

  def runCommand(cmd: String, output: Output = null): Unit = {
    val rt = Runtime.getRuntime
    output.text.text = ""
    if (output != null) {
      output.text.text += s"Running $cmd\n"
    } else
      println(s"Running $cmd\n")
    val proc = rt.exec(cmd)
    val errorReader = new StdReader(proc.getErrorStream, output)
    val outReader = new StdReader(proc.getInputStream, output)
    errorReader.start()
    outReader.start()
    sys.addShutdownHook {
      proc.destroyForcibly()
    }
  }

  def extractResource(resource: String, destPath: String = "."): Unit = {
    val source = Channels.newChannel(getClass.getClassLoader.getResourceAsStream(resource))
    val fileOut = new File(destPath, resource)
    val dest = new FileOutputStream(fileOut)
    dest.getChannel.transferFrom(source, 0, Long.MaxValue)
    source.close()
    dest.close()
  }

}
