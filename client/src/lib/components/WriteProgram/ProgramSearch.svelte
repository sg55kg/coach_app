<script lang="ts">
    import FaEllipsisV from 'svelte-icons/fa/FaEllipsisV.svelte'
    import {isMobile} from "$lib/stores/authStore.js";
    import {DisplayProgram} from "../../classes/program";
    import {ProgramService} from "../../service/ProgramService";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {userDB} from "../../stores/authStore";
    import {goto} from "$app/navigation";
    import MdClose from 'svelte-icons/md/MdClose.svelte'
    import MdSearch from 'svelte-icons/md/MdSearch.svelte'

    let viewFilters: boolean = !$isMobile
    let searchName: string = ''
    let searchResults: DisplayProgram[] = []
    let loading: boolean = false
    let success: boolean = false

    function debounce (func, timeout = 500) {
        let timer
        return (...args) => {
            clearTimeout(timer)
            timer = setTimeout(() => { func.apply(this, args) }, timeout)
        }
    }

    const searchPrograms = async () => {
        if (!searchName) return
        success = false
        loading = true
        try {
            const programResults = await ProgramService.searchProgramsByName($userDB.coachData.id, searchName)
            searchResults = programResults
            success = true
        } catch (e) {
            console.log(e)
        } finally {
            loading = false
        }
    }

    const processSearch = debounce(() => searchPrograms())

    $: success ? setTimeout(() => success = false, 2000) : null
</script>

{#if $isMobile}
    <div class="h-7 w-1/12 flex justify-end" on:click={() => viewFilters = !viewFilters}>
        <MdSearch />
    </div>
{/if}
{#if !$isMobile}
    <div class="w-full">
        <input class="bg-gray-300 p-1 w-full" placeholder="Type to search programs" bind:value={searchName} on:keyup={() => processSearch()}>
    </div>
    {#if loading}
        <div class="absolute z-[110] top-0 bottom-0 right-0 left-0 bg-gray-300 opacity-25">
        </div>
        <div class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center">
            <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
        </div>
    {/if}
    {#if searchResults.length > 0}
        <div class="absolute z-[110] top-[10em] bottom-0 right-0 left-0" on:click={() => {success = false; searchResults = []}}></div>
        <div class="absolute z-[115] top-[10em] right-0 bottom-0 left-0 bg-gray-100 px-6">
            <div class="w-full flex justify-center">
                <button class="text-lg text-red-shade" on:click={() => { searchResults.length = 0; success = false }}>
                    Return To Program
                </button>
            </div>
            {#each searchResults as result}
<!--                <a href={"/home/coach/program/" + result.id} >-->
                    <div class="m-3 rounded bg-gray-200 p-3 hover:bg-gray-400 hover:cursor-pointer shadow-md" on:click={async() => await goto(`/home/coach/program/${result.id}`)}>
                        <h3 class="text-lg font-semibold text-yellow-lt">{result.name}</h3>
                        <p>{result.athleteName ? 'Athlete: ' + result.athleteName : 'Template'}</p>
                        <p>Start Date: {result.startDate ? result.startDate.format('ddd MMMM DD') : 'None'}</p>
                        <p>End Date: {result.endDate ? result.endDate.format('ddd MMMM DD') : 'None'}</p>
                    </div>
<!--                </a>-->
            {/each}
        </div>
    {:else if success && searchResults.length < 1}
        <div class="absolute bottom-5 left-10 z-10 border-l-4 border-l-sky-400 bg-gray-200 shadow-lg shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
            No Results found
            <button class="h-8 w-8 hover:bg-gray-400 text-sky-400 rounded-full hover:text-sky-500 p-1" on:click={() => success = false}>
                <MdClose />
            </button>
        </div>
    {/if}
{/if}
{#if viewFilters && $isMobile}
    <div class="absolute h-[70vh] bg-gray-100 top-[84px] {$isMobile ? 'right-0 left-0' : 'right-3 left-3'} z-20 flex flex-col">
        <div class="w-full justify-end flex">
            <button class="h-10 w-12 px-2" on:click={() => viewFilters = false}><span><MdClose /></span></button>
        </div>
        <div class="py-2 flex items-center justify-center">
            <input type="text" class="bg-gray-300 p-1" placeholder="Search Programs" bind:value={searchName} on:keyup={() => processSearch()}>
        </div>
        {#if searchResults.length > 0}
            <div class="flex flex-col h-[70vh] overflow-y-auto pl-4">
                {#each searchResults as result}
                    <div class="bg-gray-200 p-2 rounded mx-1 my-3" on:click={async() => await goto(`/home/coach/program/${result.id}`)}>
                        <h3 class="text-lg text-yellow-lt">{result.name}</h3>
                        <p>{result.athleteName ? 'Athlete: ' + result.athleteName : 'Template'}</p>
                        <p>Start Date: {result.startDate ? result.startDate.format('ddd MMMM DD') : 'None'}</p>
                        <p>End Date: {result.endDate ? result.endDate.format('ddd MMMM DD') : 'None'}</p>
                    </div>
                {/each}
            </div>
        {/if}
        {#if loading}
            <div class="w-full h-full flex items-center justify-center">
                <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            </div>
        {/if}
        {#if success && searchResults.length < 1}
            <p class="text-center w-full text-lg font-semibold">No Results Found</p>
        {/if}
    </div>
{/if}


<style></style>