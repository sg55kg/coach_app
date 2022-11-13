const programInput = document.getElementById('program-name')
const exerciseInput = document.getElementById('exercise-name')
const weightInput = document.getElementById('weight')
const repsInput = document.getElementById('reps')
const setsInput = document.getElementById('sets')
const buttonSubmit = document.getElementById('submit')

buttonSubmit.addEventListener("click", ()=>{
    const str = exerciseInput.value + "," + weightInput.value + "," + repsInput.value + "," + setsInput.value;
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