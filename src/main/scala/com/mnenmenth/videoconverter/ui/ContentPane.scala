package com.mnenmenth.videoconverter.ui

import java.io.File

import com.mnenmenth.videoconverter.core.VideoConverter
import com.mnenmenth.videoconverter.util.Utils

import scala.io.Source
import scala.swing._

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
class ContentPane extends BoxPanel(Orientation.Vertical) {

  private def fieldTemplate = new TextField {
    columns = 20
    horizontalAlignment = Alignment.Left
  }

  val inFileLabel = new Label("File to be converted: ")
  val inFileField = fieldTemplate
  val inFileButton: Button = new Button("Browse") {
    action = Action(this.text) {
      val chooser = new FileChooser()
      chooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
      val result = chooser.showOpenDialog(null)
      result match {
        case FileChooser.Result.Approve =>
          println(chooser.selectedFile.getAbsolutePath)
          inFileField.text = chooser.selectedFile.getAbsolutePath
          if(outFolderField.text == "") outFolderField.text = chooser.selectedFile.getParent
          if(outFileField.text == "") outFileField.text = s"${chooser.selectedFile.getName.split('.')(0)}.webm"
        case _ => println("None chosen")
      }
    }
  }

  val outFolderLabel = new Label("Destination Folder: ")
  val outFolderField = fieldTemplate
  val outFolderButton = new Button("Browse") {
    action = Action(this.text) {
      val chooser = new FileChooser()
      chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
      val result = chooser.showOpenDialog(null)
      result match {
        case FileChooser.Result.Approve => outFolderField.text = chooser.selectedFile.getAbsolutePath
        case _ => println("None chosen")
      }
    }
  }

  val outFileLabel = new Label("Converted File Name")
  val outFileField = fieldTemplate

  val scaleLabel = new Label("Scale:")
  val scaleWidth = fieldTemplate
  val defaultValues = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("defaultValues.txt")).getLines().toList
  println(defaultValues)
  scaleWidth.text = defaultValues.head.trim.split('=').last
  val scaleHeight = fieldTemplate
  scaleHeight.text = defaultValues.last.trim.split('=').last

  val hGap = 10
  val vGap = 20
  val fieldGrid = new GridPanel(4, 8) {

    contents += inFileLabel
    contents += inFileField
    contents += inFileButton

    contents += outFolderLabel
    contents += outFolderField
    contents += outFolderButton

    contents += outFileLabel
    contents += outFileField
    contents += new Label("")

    contents += scaleLabel
    contents += scaleWidth
    contents += scaleHeight

    maximumSize = new Dimension(VideoConverter.windowSize.width, 60)
  }

  fieldGrid.hGap = hGap
  fieldGrid.vGap = vGap
  contents += fieldGrid

  val convert = new Button("Convert") {
    action = Action(this.text) {
      if (inFileField.text.contains('.') && outFileField.text.contains('.'))
        Utils.runCommand(s"./ffmpeg -i ${inFileField.text.trim} -c:v libvpx-vp9 -lossless 1 -crf 0 -b:v 0 -c:a libvorbis -vf scale=${scaleWidth.text.trim}:${scaleHeight.text.trim} ${outFolderField.text.trim}/${outFileField.text.trim} -y", output)
    }
  }
  contents += convert

  val output = new Output
  contents += output

  listenTo(inFileButton, outFolderButton, convert)
  val homeDir = new File(sys.props("user.home"))
}
