<!--
 * @Author: fanciNate
 * @Date: 2023-05-26 16:35:28
 * @LastEditTime: 2023-06-18 15:48:24
 * @LastEditors: fanciNate
 * @Description: In User Settings Edit
 * @FilePath: /flink-yun/flink-yun-website/src/views/workflow/work-item/running-log.vue
-->
<template>
  <div
    id="content"
    class="running-log"
  >
    <LogContainer v-if="logMsg" :logMsg="logMsg" :status="true"></LogContainer>
    <EmptyPage v-else />
  </div>
</template>

<script lang="ts" setup>
import { ref, defineExpose } from 'vue'
import EmptyPage from '@/components/empty-page/index.vue'
import { GetTaskManagerLogData } from '@/services/schedule.service'

const logMsg = ref('')
const pubId = ref('')

function initData(id: string): void {
  pubId.value = id
  getLogData(pubId.value)
}

// 获取日志
function getLogData(id: string) {
  if (!id) {
    logMsg.value = ''
    return
  }
  GetTaskManagerLogData({
    instanceId: id
  })
    .then((res: any) => {
      logMsg.value = res.data.log
    })
    .catch(() => {
      logMsg.value = ''
    })
}

defineExpose({
  initData
})
</script>

<style lang="scss">
.running-log {
  height: 100%;
  .empty-page {
    height: 100%;
  }
}
</style>
