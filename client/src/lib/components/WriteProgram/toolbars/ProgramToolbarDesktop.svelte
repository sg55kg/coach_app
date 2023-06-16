<script lang="ts">
    import {getContext} from "svelte";
    import {userDB} from "$lib/stores/authStore.js";
    import Toggle from "$lib/components/shared/layout/Toggle.svelte";
    import UserService from "../../../service/UserService";
    import {User} from "../../../classes/user";
    import FaCaretDown from 'svelte-icons/fa/FaCaretDown.svelte';
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte';
    import FaFileExport from 'svelte-icons/fa/FaFileExport.svelte';
    import FaUserPlus from 'svelte-icons/fa/FaUserPlus.svelte';
    import MdAdd from 'svelte-icons/md/MdAdd.svelte';
    import {generateCSV} from "$lib/components/WriteProgram/util/functions.js";
    import {goto} from "$app/navigation";
    import {Program} from "../../../classes/program";
    import {ProgramService} from "../../../service/ProgramService";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte';
    import {team} from "../../../stores/teamStore";
    import TemplatesModal from "$lib/components/WriteProgram/modals/TemplatesModal.svelte";
    import {page} from "$app/stores";

    export let showCreateProgram: boolean = false;
    export let showAssignAthlete: boolean = false;

    let { getProgram, getProgramLoading, updateProgram, getProgramError, getProgramSuccess } = getContext('program');
    let program = getProgram();
    let programLoading = getProgramLoading();
    let programError = getProgramError();
    let programSuccess = getProgramSuccess();

    let showFileMenu: boolean = false;
    let showTemplateImport: boolean = false;

    const makeACopy = async () => {
        if (!$program.id) return;

        let programCopy = $program.copy();
        await createProgram(programCopy);
    };

    const createProgram = async (newProgram: Program) => {
        $programError = '';
        $programSuccess = '';
        $programLoading = true;
        try {
            const res = await ProgramService.createProgram(newProgram);
            if (!res.id) {
                throw new Error('Could not create program');
            }
            $programSuccess = 'Saved!';
            $program = res;
            await goto(`/home/coach/program/${res.id}`);
        } catch (e) {
            console.log(e);
            $programError = 'Could not create this program at this time';
        } finally {
            $programLoading = false;
        }
    };

    const toggleWeightPreference = async () => {
        $programLoading = true;
        const userCopy = User.createFrom(JSON.parse(JSON.stringify($userDB)))
        if (userCopy.preferences.weight === 'kg') {
            userCopy.preferences.weight = 'lb';
        } else {
            userCopy.preferences.weight = 'kg';
        }
        try {
            const updatedUserData = await UserService.updateUserData(userCopy)
            userDB.set(updatedUserData);
        } catch (e) {
            console.log(e);
        } finally {
            $programLoading = false;
        }
    };

    const focusNameInput = () => {
        const input = document.getElementById('program-name-input');
        input.focus();
        input.classList.add('error-focus');
        input.addEventListener('focusout', () => {
            input.classList.remove('error-focus');
        });
    };

    const deleteProgram = async () => {
        $programLoading = true;
        $programSuccess = '';
        $programError = '';
        try {
            await ProgramService.deleteProgram($program.id);
            $programSuccess =
                'Successfully deleted ' +
                $program.name +
                '. Redirecting you to home...';
            $userDB.coachData.programs = $userDB.coachData.programs.filter(
                p => p.id !== $program.id
            );
            if ($team) {
                setTimeout(async () => {
                    await goto(`/home/coach/team/${$team.id}/programs`);
                }, 2000);
            } else {
                setTimeout(async () => {
                    await goto('/home');
                }, 2000);
            }
        } catch (e) {
            console.log(e);
            $programError =
                "Error, can't delete " + $program.name + ' at this time.';
        } finally {
            $programLoading = false;
        }
    };
</script>

<nav class="relative flex flex-col bg-gray-100 p-2">
    <div class="flex w-screen">
        <input
                type="text"
                class="w-3/12 rounded bg-gray-300 p-1"
                bind:value="{$program.name}"
                placeholder="Program Name"
                id="program-name-input"
        />
        {#if $program.id}<p class="px-2">
            <i
            >Last Updated: {$program.updatedAt.format(
                'ddd MMM DD YYYY hh:mm:ssA'
            )}</i
            >
        </p>{/if}
        <div class="flex self-end lg:px-2">
            <Toggle
                    checked="{$userDB.preferences.weight === 'kg'}"
                    onChange="{toggleWeightPreference}"
            />
            <p class="px-1 text-textblue">
                {$userDB.preferences.weight === 'kg' ? 'kg' : 'lbs'}
            </p>
        </div>
    </div>
    <div class="py-2">
        <button
                class="text-md mx-2 rounded bg-yellow p-1 px-2 font-medium text-gray-300 disabled:bg-gray-400"
                on:click="{updateProgram}"
                disabled="{!$program.id}"
        >
            Save
        </button>
        <div class="dropdown relative inline-block">
            <button
                    class="flex items-center rounded p-1 hover:bg-gray-400"
                    on:click="{() => (showFileMenu = !showFileMenu)}"
            >
                File
                <span class="mx-1 h-3"><FaCaretDown /></span>
            </button>
            {#if showFileMenu}
                <div
                        class="fixed top-0 right-0 left-0 bottom-0 z-[114]"
                        on:click="{() => (showFileMenu = !showFileMenu)}"
                ></div>
                <div
                        class="absolute z-[115] w-56 origin-top-right bg-gray-400 p-2 shadow-lg"
                        on:click="{() =>
                                setTimeout(() => (showFileMenu = false), 100)}"
                >
                    <!--                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200 disabled:text-gray-100 disabled:hover:bg-gray-400">-->
                    <!--                                <span class="h-5 mr-2"><FaRegSave /></span>-->
                    <!--                                Save-->
                    <!--                            </button>-->
                    <button
                            class="flex w-full items-center p-2 text-left hover:bg-gray-200"
                            on:click="{() => (showCreateProgram = true)}"
                    >
                        <span class="mr-2 h-5"><MdAdd /></span>
                        New
                    </button>
                    <button
                            class="flex w-full items-center p-2 text-left hover:bg-gray-200"
                            on:click="{makeACopy}"
                    >
                        <span class="mr-2 h-5"><FaRegCopy /></span>
                        Make a copy
                    </button>
                    <button
                            class="flex w-full items-center p-2 text-left hover:bg-gray-200"
                            on:click="{generateCSV}"
                    >
                        <span class="mr-2 h-5"><FaFileExport /></span>
                        Export to CSV
                    </button>
                    <button
                            class="flex w-full items-center p-2 text-left hover:bg-gray-200"
                            on:click="{() =>
                                    $program.name
                                        ? (showAssignAthlete = true)
                                        : focusNameInput()}"
                    >
                        <span class="mr-2 h-5"><FaUserPlus /></span>
                        Assign to athlete
                    </button>
                    <hr />
                    <button
                            class="flex w-full items-center p-2 text-left text-red-shade hover:bg-gray-200"
                            on:click="{deleteProgram}"
                    >
                        <span class="mr-2 h-5"><FaTrashAlt /></span>
                        Delete Program
                    </button>
                </div>
            {/if}
        </div>
        {#if !$program.id}
            <button class="rounded p-1 hover:bg-gray-400" on:click={() => showTemplateImport = !showTemplateImport}>
                Import Template
            </button>
        {/if}
        {#if $program.id && $page.url.pathname.includes('stats')}
            <a href="/home/coach/program/{$program.id}">
                <button> Calendar View </button>
            </a>
        {:else}
            <a href="/home/coach/program/{$program.id}/stats">
                <button> Program Stats </button>
            </a>
        {/if}
    </div>
</nav>
{#if showTemplateImport}
    <TemplatesModal bind:show={showTemplateImport} />
{/if}

<style></style>