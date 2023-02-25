package com.blogspot.thengnet.mobilestudent

class Audio {
    var audioTitle: String? = null
        private set
    var audioPath: String? = null
        private set
    var audioLength: String? = null
        private set

    constructor() {}
    constructor(theAudioTitle: String?, theAudioPath: String?, theAudioLength: String?) {
        audioTitle = theAudioTitle
        audioPath = theAudioPath
        audioLength = theAudioLength
    }
}