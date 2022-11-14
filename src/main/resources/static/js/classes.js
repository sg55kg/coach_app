class Program {
    constructor() {
    }

    static createProgram(name, days) {
        let program = new Program()
        program.name = name
        program.days = days

        return program
    }

    name = ""
    days = []
}

class Day {
    constructor() {

    }

    static createDay(exercises, date) {
        let day = new Day()
        day.exercises = exercises;
        day.date = date;

        return day
    }

    exercises = []
    date = Date.now()
}

class Exercise {
    constructor() {
    }

    static createExercise(name, weight, sets, repsPerSet) {
        let exercise = new Exercise()
        exercise.name = name
        exercise.weight = weight
        exercise.sets = sets
        exercise.repsPerSet = repsPerSet

        return exercise
    }

    name = ""
    weight = 0
    sets = 0
    repsPerSet = 0
}