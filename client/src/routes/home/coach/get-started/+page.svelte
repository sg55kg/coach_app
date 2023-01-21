<script lang="ts">

    import {userDB} from "$lib/stores/authStore";
    import {User} from "$lib/classes/user";
    import {goto} from "$app/navigation";

    let teamName: string = ''
    let description: string = ''
    let imagePreviewSrc: string = ''

    const handleSubmit = async () => {
        if(!$userDB) return

        const newCoach = {
            team: {
                name: teamName,
                description: description,
                teamLogo: imagePreviewSrc
            },
            userId: $userDB.id
        }

        try {
            const res = await fetch('/api/coach', {
                method: 'POST',
                body: JSON.stringify(newCoach)
            })
            const updatedUser = await res.json()
            if (updatedUser.coachData) {
                $userDB = User.build(updatedUser)
                await goto(`/home/coach/${$userDB.coachData!.id}`)
            }
        } catch (e) {
            console.log(e)
        }
    }
</script>

<div class="flex flex-col p-3 px-5 lg:p-10 lg:px-10 lg:w-9/12 m-auto bg-gray-400 shadow-2xl w-11/12 rounded">
    <div class="text-center font-bold text-2xl text-textblue mb-2">
        <h1>Let's get started!</h1>
    </div>
    <div class="text-center font-semibold text-md">
        <h2>Create your first team</h2>
    </div>
    <div class="flex flex-col m-2">
        <label>Team Name</label>
        <input bind:value={teamName}
               name="team-name"
               placeholder="Team Name"
               class="placeholder-gray-400 bg-gray-200 p-1 text-textblue font-semibold"
               type="text"
        />
    </div>
    <div class="flex flex-col m-2">
        <label>Description</label>
        <textarea name="description"
                  bind:value={description}
                  placeholder="Tell us about your team, your experience coaching, etc"
                  class="placeholder-gray-400 bg-gray-200 p-1 text-textblue font-semibold"
                  type="text"></textarea>
    </div>
    <div class="flex flex-col m-2">
        <label>Link to your logo</label>
        <input bind:value={imagePreviewSrc}
               placeholder="Ex: https://myimage.com/img.jpg"
               name="logo-link"
               class="placeholder-gray-400 bg-gray-200 p-1 text-textblue font-semibold"
               type="text"
        />
    </div>
    <div class="flex m-2 justify-center">
        {#if imagePreviewSrc}
            <img class="w-9/12 lg:w-7/12" alt="Your Team Logo" src={imagePreviewSrc}>
        {/if}
    </div>
    <div class="flex flex-col lg:flex-row lg:justify-center my-3">
        <button on:click={handleSubmit} class="bg-yellow hover:bg-yellow-shade text-black font-semibold p-2 lg:px-6 rounded shadow-md shadow-gray-200">
            Save
        </button>
    </div>
</div>

<style>

</style>