
// this is an anonymous, self-calling function
(async () => {
    const fetchAllPrograms = async () => {
        const programs = await ProgramService.getAllPrograms();
        return programs;
    }

    const programsArr = await fetchAllPrograms();

    const generateProgramLinks = (programsArr) => {
        for (const program of programsArr) {
            const listItem = document.createElement('li');
            const programLink = document.createElement('a');

            programLink.href = "http://localhost:8080/" + program.id;
            programLink.innerText = program.name;

            listItem.appendChild(programLink);
            programsList.appendChild(listItem);
        }
    }

    generateProgramLinks(programsArr);
})();

