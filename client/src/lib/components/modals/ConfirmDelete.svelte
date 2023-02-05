<script lang="ts">
    export let show: boolean = false
    export let deleteFunction: (...args: any[]) => Promise<any>
    export let confirmationMessage: string = 'Are you sure you want to delete this?'
    export let subheader: string = ''
</script>

{#if show}
<div class="fixed inset-0 bg-black bg-opacity-50 transition-opacity w-full duration-500">
<!--    <div class="m-auto bg-gray-100 h-full shadow-xl delay-200 duration-200">-->

        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
            <div class="flex flex-col relative transform overflow-hidden rounded-lg bg-gray-200 sm:justify-items-center md:text-left shadow-2xl transition-all sm:my-4 w-8/12 md:w-4/12">

                <div class="bg-gray-200 px-4 lg:pt-6 lg:pb-3 p-6">
                    <div class="sm:flex md:items-start flex-col text-lg">
                        <h3>{confirmationMessage}</h3>
                        <small><i>{subheader}</i></small>
                    </div>
                </div>
                <div class="bg-gray-50 px-2 py-3 mr-3 w-4/12 justify-around lg:flex lg:flex-row lg:px-2 self-end">
                    <button class="hover:bg-gray-200 text-link" on:click={() => show = false}>
                        Cancel
                    </button>
                    <button class="bg-red hover:bg-red-shade p-2 rounded" on:click={deleteFunction}>
                        Confirm
                    </button>
                </div>
            </div>
        </div>
<!--    </div>-->
</div>
{/if}
<style></style>