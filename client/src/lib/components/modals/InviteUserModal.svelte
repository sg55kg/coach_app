<script lang="ts">
    import { page } from "$app/stores";
    import UserService from "$lib/service/UserService";
    
    export let showModal: boolean = false;   
    let success: boolean = false;
    let error: boolean = false;
    
    let response = {};
    const sendInvite = async () => {
        try {
            const inviteUserRequest = {
                coachEmail: '',
                atheleteEmail: '',
                athleteName: '',
                coachName: '',
                teamId: '',
                coachId: '',
            };
            response = await UserService.inviteUser(inviteUserRequest);
            console.log(response);
            return success = true;
        } catch (e) {
            console.log(e);
            return error = true;
        }
    };
</script>
{#if showModal}
            <div>
                <input type="text" placeholder="Name" />Name
                <input type="text" placeholder="Email" />Email Address
                <button on:click="{sendInvite}">Send</button>
                {#if success}
                    <div>Invitation sent!</div>
                {/if}
                {#if error}
                    <div>The invitation failed to send.</div>
                {/if}
            </div>
        {/if}
<style>    
</style>