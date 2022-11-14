class Program {
    constructor() {
    }

    static createProgram(name, days, startDate, endDate) {
        let program = new Program()
        program.name = name
        program.days = days
        program.startDate = startDate
        program.endDate = endDate
        return program
    }

    name = ""
    days = []
    startDate = Date.now()
    endDate = Date.now()
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

    static createExercise(name, weight, sets, repsPerSet, notes) {
        let exercise = new Exercise()
        exercise.name = name
        exercise.weight = weight
        exercise.sets = sets
        exercise.repsPerSet = repsPerSet
        exercise.notes = notes

        return exercise
    }

    name = ""
    weight = 0
    sets = 0
    repsPerSet = 0
    notes = ""
}