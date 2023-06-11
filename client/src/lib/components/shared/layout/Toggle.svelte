<script lang="ts">
    export let checked: boolean = false;
    export let onChange: (...args: any[]) => any = () => null;
    export let disabled: boolean = false;
</script>

<label class="switch {disabled ? 'disabled' : ''}">
    <input
        type="checkbox"
        disabled="{disabled}"
        checked="{checked}"
        on:change="{onChange}"
    />
    <span class="slider round {disabled ? 'disabled' : ''}"></span>
</label>

<style>
    .switch {
        position: relative;
        display: inline-block;
        width: 55px;
        height: 28px;
    }

    /* Hide default HTML checkbox */
    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: 0.4s;
        transition: 0.4s;
    }

    .slider:before {
        position: absolute;
        content: '';
        height: 20px;
        width: 20px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: 0.4s;
        transition: 0.4s;
    }

    .disabled {
        cursor: not-allowed !important;
    }

    input:checked + .slider {
        background-color: #fde577;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #fde577;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }
</style>
