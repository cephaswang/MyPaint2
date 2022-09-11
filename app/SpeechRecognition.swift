//
//  ViewController.swift
//  NySiri
//
//  Created by admin on 2022/9/11.
//

/*

Use Mic & Speech Recognition in App (Swift 5) Xcode 11 - 2020
https://www.youtube.com/watch?v=vg2_b_jrorU

https://gist.github.com/jacobbubu/1836273
 
*/

import UIKit
import InstantSearchVoiceOverlay

class ViewController: UIViewController {

    lazy var voiceOverlay: VoiceOverlayController = {
      let recordableHandler = {
        return SpeechController(locale: Locale(identifier: "zh_TW"))
      }
      return VoiceOverlayController(speechControllerHandler: recordableHandler)
    }()

    
    @IBOutlet var myButton : UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        myButton.backgroundColor = .systemRed
        myButton.setTitleColor(.white, for: .normal)
    }

    @IBAction func didTabButton(){
        
        let identifiers : NSArray = NSLocale.availableLocaleIdentifiers as NSArray
        let locale = NSLocale(localeIdentifier: "en_US")
        let list = NSMutableString()
        for identifier in identifiers {
            let name = locale.displayName(forKey: NSLocale.Key.identifier, value: identifier)!
            list.append("\(identifier)\t\(name)\n")
        }
        print(list)

        voiceOverlay.start(on: self, textHandler: { text ,final , _ in
            if final{
                print("Final text:\(text)")
            } else {
                print("In processes:\(text)")
            }
        },errorHandler:{error in})
    }
    

}

