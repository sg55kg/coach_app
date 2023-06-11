<script lang="ts">
    import GeneralSettings from './GeneralSettings.svelte';
    import PaymentSettings from './PaymentSettings.svelte';

    const pageOptions = [
        { name: 'general', component: GeneralSettings },
        { name: 'payment', component: PaymentSettings },
    ];

    let selectedPage = pageOptions[0];
</script>

<div class="flex h-full w-full flex-col p-3 lg:flex-row">
    <div class="flex flex-row lg:w-2/12 lg:flex-col lg:items-start">
        <button
            class="p-2 text-lg font-semibold {selectedPage.name === 'general'
                ? 'text-link'
                : 'text-textblue'}"
            on:click="{() => (selectedPage = pageOptions[0])}"
            >General Settings</button
        >
        <button
            class="p-2 text-lg font-semibold {selectedPage.name === 'payment'
                ? 'text-link'
                : 'text-textblue'}"
            on:click="{() => (selectedPage = pageOptions[1])}">Your $$</button
        >
        <button
            class="p-2 text-lg font-semibold {selectedPage.name === 'athletes'
                ? 'text-link'
                : 'text-textblue'}">Team Athletes</button
        >
    </div>
    <svelte:component this="{selectedPage.component}" />
</div>

<style>
</style>
