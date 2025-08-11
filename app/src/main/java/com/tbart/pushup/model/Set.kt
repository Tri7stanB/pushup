package com.tbart.pushup.model

class Set {
    var reps: Int = 0
    var weight: Int = 0
    var duration: Int = 0 // in seconds
    var rest: Int = 0 // in seconds

    constructor(reps: Int, weight: Int, duration: Int, rest: Int) {
        this.reps = reps
        this.weight = weight
        this.duration = duration
        this.rest = rest
    }

}