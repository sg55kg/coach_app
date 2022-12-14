(async () => {

let program;
let newDay;
let currentDayIdx = 0;

const loadBlankProgram = () => {
    program = new Program()
    newDay = new Day()

    program.days.push(newDay)
}

const clearExerciseTable = () => {
    while (dayExercise.children.length > 0) {
        dayExercise.removeChild(dayExercise.lastElementChild)
    }
}

const fillExerciseTable = (exerciseArr) => {
    for (const exercise of exerciseArr) {
        let newRow = document.createElement('tr')
        for (const val of Object.values(exercise)) {
            let newColumn = document.createElement('td')
            newColumn.innerText = val
            newRow.appendChild(newColumn)
        }
        dayExercise.appendChild(newRow)
    }
}


if (window.location.pathname === "/create") {
    loadBlankProgram();
} else {
    let programResponse = await ProgramService.getProgram(window.location.pathname.replace('/', ''))
    program = programResponse
    console.log(programResponse)
    if(program.days.length > 0) {
        fillExerciseTable(program.days[currentDayIdx].exercises)
    }
    programInput.value = program.name
    startDateInput.value = program.startDate
    endDateInput.value = program.endDate
    nextDayBtn.disabled = false
}

const clearExerciseInputs = () => {
    exerciseInput.value = ''
    weightInput.value = ''
    repsInput.value = ''
    setsInput.value = ''
    notesInput.value = ''
}

const appendExercise = () => {
    const exercise = Exercise.createExercise(
        exerciseInput.value,
        weightInput.value,
        repsInput.value,
        setsInput.value,
        notesInput.value)
    let currentDay = program.days[currentDayIdx]
    currentDay.exercises.push(exercise)

    let newRow = document.createElement('tr')

    for (const field of Object.values(exercise)) {
        let newColumn = document.createElement('td')
        newColumn.innerText = field
        newRow.appendChild(newColumn)
    }

    dayExercise.appendChild(newRow)
    clearExerciseInputs()
}

addDayBtn.addEventListener('click', () => {
    const dayNode = document.createElement('p')
    dayNode.innerText = "Day " + program.days.length
    daysList.appendChild(dayNode)
    currentDayIdx++
    clearExerciseTable()
    clearExerciseInputs()

    const newDay = new Day()
    program.days.push(newDay)
    prevDayBtn.disabled = false
    exerciseInput.focus()
})

prevDayBtn.addEventListener('click', () => {
    if (currentDayIdx === 0) return

    nextDayBtn.disabled = false
    clearExerciseTable()
    clearExerciseInputs()
    currentDayIdx--
    fillExerciseTable(program.days[currentDayIdx].exercises)

    if (currentDayIdx === 0) {
        prevDayBtn.disabled = true
    }
})

nextDayBtn.addEventListener('click', () => {
    if (currentDayIdx === program.days.length - 1) return

    prevDayBtn.disabled = false
    clearExerciseTable()
    clearExerciseInputs()
    currentDayIdx++
    fillExerciseTable(program.days[currentDayIdx].exercises)

    if (currentDayIdx === program.days.length - 1) {
        nextDayBtn.disabled = true
    }
})

programInput.addEventListener("change",(e) => {
    program.name = e.target.value
})

startDateInput.addEventListener("change",(e) => {
    program.startDate = new Date(e.target.value)
})

endDateInput.addEventListener("change",(e) => {
    program.endDate = new Date(e.target.value)
})

notesInput.addEventListener('keyup', (e) => {
    if(e.key === 'Enter') {
        if (notesInput.value.endsWith('\n')) {
            // remove \n that is still inserted on Enter press
            notesInput.value = notesInput.value.replaceAll('\n', ' ')
        }
        appendExercise()
        exerciseInput.focus()
    }
})

buttonAdd.addEventListener("click", appendExercise)

buttonSubmit.addEventListener("click", async () => {
    if (program.id != null) {
        await ProgramService.updateProgram(program)
    } else {
        console.log(program)
        const savedProgram = await ProgramService.saveProgram(program)
        console.log(savedProgram)
    }
    generateCSV()
})

const generateCSV = () => {
    let str = "Day 1\nExercise,Weight,Sets,Reps,Notes\n";
    for(let i=0;i< program.days.length; i++) {
        if(program.days[i].exercises.length < 1) continue
        if (i > 0) str += "Day " + (i+1) + "\n"
        const d = program.days[i]
        for(let j = 0; j < d.exercises.length; j++) {
            str += d.exercises[j].name + "," +
                d.exercises[j].weight + "," +
                d.exercises[j].repsPerSet + "," +
                d.exercises[j].sets + "," +
                d.exercises[j].notes + "\n"
        }
    }

    const downloadLink = document.getElementById('download-btn');
    const csv = str
    const blob = new Blob(["\ufeff", csv], {type: 'text/csv'});
    const url = URL.createObjectURL(blob);
    const exportFilename = programInput.value;
    downloadLink.href = url;
    downloadLink.setAttribute('download', exportFilename);
    downloadLink.hidden = false;
}


})();