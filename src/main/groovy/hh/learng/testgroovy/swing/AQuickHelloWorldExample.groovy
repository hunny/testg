package hh.learng.testgroovy.swing

import static javax.swing.JFrame.EXIT_ON_CLOSE
import java.awt.event.ActionEvent

class AQuickHelloWorldExample {

	static main(args) {
		groovy.swing.SwingBuilder.build {
			root = frame(title:'Hello World', show:true, pack:true, defaultCloseOperation: EXIT_ON_CLOSE) {
				flowLayout()
				label('Enter a message:')
				mField = textField(id:'name', columns:20)
				button('Click', actionPerformed: { ActionEvent e ->
					if (mField.text) {
						optionPane().showMessageDialog(root, mField.text)
					}
				})
			}
		}
	}

}
