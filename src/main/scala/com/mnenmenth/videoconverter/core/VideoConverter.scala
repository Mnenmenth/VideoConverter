package com.mnenmenth.videoconverter.core

import java.awt.Dimension

import com.mnenmenth.videoconverter.ui.ContentPane
import com.mnenmenth.videoconverter.util.Utils

import scala.swing.{MainFrame, SimpleSwingApplication}

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object VideoConverter extends SimpleSwingApplication {

  val windowSize = new Dimension(800, 600)

  def top = new MainFrame {
    title = "Video Converter"
    size = windowSize
    maximumSize = windowSize
    minimumSize = windowSize
    contents = new ContentPane
    Utils.extractResource("ffmpeg.exe")
  }

}
