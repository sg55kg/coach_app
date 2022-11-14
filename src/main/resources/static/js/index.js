
const program = new Program()
const newDay = new Day()
let currentDayIdx = 0
program.days.push(newDay)

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

const clearExerciseInputs = () => {
    exerciseInput.value = ''
    weightInput.value = ''
    repsInput.value = ''
    setsInput.value = ''
    notesInput.value = ''
}

addDayBtn.addEventListener('click', () => {
    const dayNode = document.createElement('p')
    dayNode.innerText = "Day " + program.days.length + " " + startDateInput.value
    daysList.appendChild(dayNode)
    currentDayIdx++
    clearExerciseTable()
    clearExerciseInputs()

    const newDay = new Day()
    program.days.push(newDay)
    prevDayBtn.disabled = false
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

buttonAdd.addEventListener("click", () => {
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

})

buttonSubmit.addEventListener("click", ()=>{

    let str = "Day 1\nExercise,Weight,Reps,Sets,Notes\n";
    for(let i=0;i< program.days.length; i++) {
        if(program.days[i].exercises.length < 1) continue
        if (i > 0) str += "Day " + (i+1) + "\n"
        const d = program.days[i]
        for(let j = 0; j < d.exercises.length; j++) {
            str += d.exercises[j].name + "," +
                d.exercises[j].weight + "," +
                d.exercises[j].sets + "," +
                d.exercises[j].repsPerSet + "," +
                d.exercises[j].notes + "\n"
        }
    }

    const downloadLink = document.createElement("a");
    const csv = str
    const blob = new Blob(["\ufeff", csv], {type: 'text/csv'});
    const url = URL.createObjectURL(blob);
    const exportFilename = programInput.value;
    downloadLink.href = url;
    downloadLink.setAttribute('download', exportFilename);
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
})