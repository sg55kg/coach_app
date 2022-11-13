console.log("This is the JS file")
const exerciseInput = document.getElementById('exercise-name')
const weightInput = document.getElementById('weight')
const repsInput = document.getElementById('reps')
const setsInput = document.getElementById('sets')
const buttonSubmit = document.getElementById('submit')

const str = exerciseInput.value + "," + weightInput.value + "," + repsInput.value + "," + setsInput.value;

buttonSubmit.addEventListener("click", ()=>{
    const str = exerciseInput.value + "," + weightInput.value + "," + repsInput.value + "," + setsInput.value;
    console.log(str)
    var downloadLink = document.createElement("a");
    var csv = str
    var blob = new Blob(["\ufeff", csv]);
    var url = URL.createObjectURL(blob);
    downloadLink.href = url;
    downloadLink.download = "data.csv";

    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
})