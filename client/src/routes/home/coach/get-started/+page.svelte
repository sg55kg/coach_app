<script lang="ts">
    import { userDB } from '$lib/stores/authStore';
    import { User } from '$lib/classes/user';
    import { goto } from '$app/navigation';

    let teamName: string = '';
    let description: string = '';
    let imagePreviewSrc: string = '';

    const handleSubmit = async () => {
        if (!$userDB) return;
        const newCoach = {
            team: {
                name: teamName,
                description: description,
                teamLogo: imagePreviewSrc,
            },
            userId: $userDB.id,
        };

        try {
            const res = await fetch('/api/coach', {
                method: 'POST',
                body: JSON.stringify(newCoach),
            });
            const updatedUser = await res.json();
            if (updatedUser.coachData) {
                $userDB = User.build(updatedUser);
                await goto(`/home/coach/${$userDB.coachData!.id}`);
            }
        } catch (e) {
            console.log(e);
        }
    };
</script>

<div
    class="m-auto flex w-11/12 flex-col rounded bg-gray-400 p-3 px-5 shadow-2xl lg:w-9/12 lg:p-10 lg:px-10"
>
    <div class="mb-2 text-center text-2xl font-bold text-textblue">
        <h1>Let's get started!</h1>
    </div>
    <div class="text-md text-center font-semibold">
        <h2>Create your first team</h2>
    </div>
    <div class="m-2 flex flex-col">
        <label>Team Name</label>
        <input
            bind:value="{teamName}"
            name="team-name"
            placeholder="Team Name"
            class="bg-gray-200 p-1 font-semibold text-textblue placeholder-gray-400"
            type="text"
        />
    </div>
    <div class="m-2 flex flex-col">
        <label>Description</label>
        <textarea
            name="description"
            bind:value="{description}"
            placeholder="Tell us about your team, your experience coaching, etc"
            class="bg-gray-200 p-1 font-semibold text-textblue placeholder-gray-400"
            type="text"></textarea>
    </div>
    <div class="m-2 flex flex-col">
        <label>Link to your logo</label>
        <input
            bind:value="{imagePreviewSrc}"
            placeholder="Ex: https://myimage.com/img.jpg"
            name="logo-link"
            class="bg-gray-200 p-1 font-semibold text-textblue placeholder-gray-400"
            type="text"
        />
    </div>
    <div class="m-2 flex justify-center">
        {#if imagePreviewSrc}
            <img
                class="w-9/12 lg:w-7/12"
                alt="Your Team Logo"
                src="{imagePreviewSrc}"
            />
        {/if}
    </div>
    <div class="my-3 flex flex-col lg:flex-row lg:justify-center">
        <button
            on:click="{handleSubmit}"
            class="rounded bg-yellow p-2 font-semibold text-black shadow-md shadow-gray-200 hover:bg-yellow-shade lg:px-6"
        >
            Save
        </button>
    </div>
</div>

<style>
</style>
