const programInput = document.getElementById('program-name')
const exerciseInput = document.getElementById('exercise-name')
const weightInput = document.getElementById('weight')
const repsInput = document.getElementById('reps')
const setsInput = document.getElementById('sets')
const buttonSubmit = document.getElementById('submit')
const buttonAdd = document.getElementById('add-exercise')
const dayExercise = document.getElementById('day-exercise')
const fullProgram =document.getElementById('full-program')
const programArray = []

let programInputValue = ""
programInput.addEventListener("change",(e) =>{
    programInputValue = e.target.value;
    fullProgram.innerHTML = programInputValue;
})

buttonAdd.addEventListener("click", ()=>{
    const exerciseObject = {
        exercise:exerciseInput.value,
        weight:weightInput.value,
        reps:repsInput.value,
        sets:setsInput.value
    };
    programArray.push(exerciseObject);
    console.log(programArray);
    let textofArray = "";
    for(let i=0;i< programArray.length; i++) {
        textofArray += "<p>exercise:" + programArray[i].exercise + " weight:" + programArray[i].weight +" reps:"+ programArray[i].reps +" sets:"+ programArray[i].sets+"</p>";
    }
    dayExercise.innerHTML = textofArray
})

buttonSubmit.addEventListener("click", ()=>{
    //const str = exerciseInput.value + "," + weightInput.value + "," + repsInput.value + "," + setsInput.value;
    let str = "";
    for(let i=0;i< programArray.length; i++) {
        str += programArray[i].exercise + "," + programArray[i].weight + "," + programArray[i].reps + "," + programArray[i].sets + "\n";
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