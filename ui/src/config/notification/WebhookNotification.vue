<template>
  <template v-if="notificationConfig['notificationType'] === 'WEB_HOOK'">
    <el-form-item label="通知模版">
      <el-input v-model:model-value="props.notificationConfig['notificationTemplate']" :autosize="{ minRows: 2 }"
        placeholder="${notification}" type="textarea" />
    </el-form-item>
    <el-form-item label="Method">
      <el-select v-model:model-value="props.notificationConfig['webHookMethod']">
        <el-option v-for="item in ['POST', 'GET', 'PUT', 'DELETE']" :key="item" :label="item" :value="item" />
      </el-select>
    </el-form-item>
    <el-form-item label="URL">
      <el-input v-model:model-value="props.notificationConfig['webHookUrl']" type="textarea" autosize
        placeholder="https://www.xxx.com?text=test_${notification}" />
    </el-form-item>
    <el-form-item label="Body">
      <el-input v-model:model-value="props.notificationConfig['webHookBody']" type="textarea" :autosize="{ minRows: 2 }"
        placeholder='{"text":"test_${notification}"}' />
    </el-form-item>
    <el-form-item v-for="(config, index) in props.notificationConfig['webHookHeaders']" :label="'Header' + (index + 1)"
      :key="config.key + index" :prop="'config.' + index + '.value'">
      <el-input v-model="config.key" autosize></el-input>
      <el-input v-model="config.value" autosize></el-input>
      <el-button @click.prevent="removeHeader(config)">Delete</el-button>
    </el-form-item>
    <el-button @click.prevent="addHeader()">Add Header</el-button>
    <div style="display: flex; justify-content: end">
      <a href="https://docs.wushuo.top/config/notification" target="_blank">通知模版示例</a>
    </div>
  </template>
</template>

<script setup>

let props = defineProps(["notificationConfig", "config"]);
let addHeader = () => {
  props.notificationConfig['webHookHeaders'].push({
    value: '',
    key: '',
  });
}
let removeHeader = (config) => {
  var index = props.notificationConfig['webHookHeaders'].indexOf(config)
  if (index !== -1) {
    props.notificationConfig['webHookHeaders'].splice(index, 1)
  }
}
</script>
