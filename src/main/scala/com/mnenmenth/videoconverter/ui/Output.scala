package com.mnenmenth.videoconverter.ui

import scala.swing.{ScrollPane, TextArea}

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
class Output extends ScrollPane {

  var text = new TextArea {
    editable = false
  }

  contents = text

}
