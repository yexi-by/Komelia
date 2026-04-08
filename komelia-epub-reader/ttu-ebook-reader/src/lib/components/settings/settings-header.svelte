<script lang="ts">
  import {faArrowLeft} from '@fortawesome/free-solid-svg-icons';
  import Fa from 'svelte-fa';
  import {baseHeaderClasses, baseIconClasses} from '$lib/css-classes';
  import {dummyFn} from "$lib/functions/utils";
  import {epubStrings$} from '$lib/i18n/strings';

  interface Props {
    onExit: () => void;
  }

  let {onExit}: Props = $props()


</script>

<div class={baseHeaderClasses}>
  <div class="flex justify-end md:px-8">
    <div
        tabindex="0"
        role="button"
        title={$epubStrings$.settings.return}
        class={baseIconClasses}
        onclick={onExit}
        onkeyup={dummyFn}
    >
      <Fa icon={faArrowLeft}/>
    </div>
  </div>
</div>
