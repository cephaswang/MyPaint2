//
//  C07audioPlayer.swift
//  AudioBible WatchKit Extension
//
//  Created by admin on 2022/4/17.
//

import SwiftUI
import AVFoundation

struct C07audioPlayer: View {
    @State var audioPlayer: AVAudioPlayer!
    var del:AVAudioPlayerDelegate?
    @State var progress7:CGFloat = 0
    @State var durLabel_text:String = ""
    @State var curLabel_text:String = ""
    var scaleVar:CGFloat = 120
    
    var body: some View {
        VStack {

            Text("AVAudioPlayer")
        
        ZStack{
            
            
            Circle()
                .fill(Color.gray.opacity(0.3))
            
                ProgressShape(progress: progress7)
                .fill(Color.gray.opacity(0.4))
                .rotationEffect(.init(degrees: -90))
            HStack {
                Text(curLabel_text)
                    .frame(width: scaleVar * 0.45, height: scaleVar * 0.45, alignment: .leading)
                
                Text(durLabel_text)
                    .frame(width: scaleVar * 0.45, height: scaleVar * 0.45, alignment: .leading)
                
            }.frame(width: scaleVar, height: scaleVar, alignment: .center)
            .onAppear(){
            
                let sound = Bundle.main.path(forResource: "01000", ofType: "mp3")
                self.audioPlayer = try! AVAudioPlayer(contentsOf: URL(fileURLWithPath: sound!))
                self.audioPlayer.delegate = self.del
                self.audioPlayer.enableRate = true
                self.audioPlayer.prepareToPlay()
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                    self.audioPlayer.play()
                }
                
                Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { (_) in
                    
                    if self.audioPlayer.isPlaying{
                        progress7 = self.audioPlayer.currentTime / self.audioPlayer.duration
                        
                        // this is to compute and show remaining time
                        let duration = Int((audioPlayer.duration - (audioPlayer.currentTime)))
                        let minutes2 = duration/60
                        let seconds2 = duration - minutes2 * 60
                        durLabel_text = NSString(format: "%02d:%02d", minutes2,seconds2) as String

                        //This is to show and compute current time
                        let currentTime1 = Int((audioPlayer.currentTime))
                        let minutes = currentTime1/60
                        let seconds = currentTime1 - minutes * 60
                        curLabel_text = NSString(format: "%02d:%02d", minutes,seconds) as String

                    }
                }

            }

        }
        .frame(width: scaleVar, height: scaleVar)

        }
    }
}

struct C07audioPlayer_Previews: PreviewProvider {
    static var previews: some View {
        C07audioPlayer()
    }
}

struct ProgressShape7:Shape {
    var progress:CGFloat
    var scaleVar_Helf:CGFloat = 60
    func path(in rect: CGRect) -> Path {
        return Path{path in path.move(to: CGPoint(x: rect.midX, y: rect.midY) )
            path.addArc(center: CGPoint(x: rect.midX, y: rect.midY), radius: scaleVar_Helf, startAngle: .zero, endAngle: .init(degrees: Double(progress * 360)), clockwise: false)
        }
    }
}
