<template>
    <div class="zqy-work-item zqy-flink-jar">
        <div class="header-options">
            <div class="btn-box" @click="goBack">
                <el-icon>
                    <RefreshLeft />
                </el-icon>
                <span class="btn-text">返回</span>
            </div>
            <div class="btn-box" @click="runWorkData">
                <el-icon v-if="!runningLoading">
                    <VideoPlay />
                </el-icon>
                <el-icon v-else class="is-loading">
                    <Loading />
                </el-icon>
                <span class="btn-text">运行</span>
            </div>
            <div v-if="workConfig.workType === 'FLINK_JAR'" class="btn-box" @click="terWorkData">
                <el-icon v-if="!terLoading">
                    <Close />
                </el-icon>
                <el-icon v-else class="is-loading">
                    <Loading />
                </el-icon>
                <span class="btn-text">中止</span>
            </div>
            <div class="btn-box" @click="saveData">
                <el-icon v-if="!saveLoading">
                    <Finished />
                </el-icon>
                <el-icon v-else class="is-loading">
                    <Loading />
                </el-icon>
                <span class="btn-text">保存</span>
            </div>
            <div class="btn-box" @click="setConfigData">
                <el-icon>
                    <Setting />
                </el-icon>
                <span class="btn-text">配置</span>
            </div>
            <div class="btn-box" @click="locationNode">
                <el-icon>
                    <Position />
                </el-icon>
                <span class="btn-text">定位</span>
            </div>
        </div>
        <LoadingPage :visible="loading" :network-error="networkError" @loading-refresh="initData">
            <div class="zqy-work-container jar-work-container">
                <div class="sql-code-container">
                    <!-- 这里是表单部分 -->
                    <el-form ref="form" label-position="top" label-width="70px" :model="jarJobConfig" :rules="rules">
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="appName" label="应用名称">
                                    <el-input v-model="jarJobConfig.appName" clearable placeholder="请输入"
                                        maxlength="200"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="jarFileId" label="资源文件">
                                    <el-select v-model="jarJobConfig.jarFileId" clearable filterable placeholder="请选择">
                                        <el-option v-for="item in fileIdList" :key="item.value" :label="item.label" :value="item.value" />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="mainClass" label="mainClass">
                                    <el-input v-model="jarJobConfig.mainClass" clearable placeholder="请输入"
                                        maxlength="200"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="请求参数" class="jar-args-container">
                            <el-icon class="button-add" @click="addParam(jarJobConfig.args)"><CirclePlusFilled /></el-icon>
                            <el-scrollbar>
                                <template v-for="(tag, index) in jarJobConfig.args" :key="index">
                                    <div class="input-container">
                                        <el-input v-model="jarJobConfig.args[index]" clearable placeholder="请输入" maxlength="2000" @blur.stop></el-input>
                                        <el-icon class="button-remove" @click="handleClose(index)"><RemoveFilled /></el-icon>
                                    </div>
                                </template>
                            </el-scrollbar>
                        </el-form-item>
                    </el-form>
                </div>
                <el-collapse v-model="collapseActive" class="work-item-log__collapse" ref="logCollapseRef">
                    <el-collapse-item title="查看日志" :disabled="true" name="1">
                        <template #title>
                        <el-tabs v-model="activeName" @tab-click="changeCollapseUp" @tab-change="tabChangeEvent">
                            <template v-for="tab in tabList" :key="tab.code">
                            <el-tab-pane v-if="!tab.hide" :label="tab.name" :name="tab.code" />
                            </template>
                        </el-tabs>
                        <span class="log__collapse">
                            <el-icon v-if="isCollapse" @click="changeCollapseDown">
                            <ArrowDown />
                            </el-icon>
                            <el-icon v-else @click="changeCollapseUp">
                            <ArrowUp />
                            </el-icon>
                        </span>
                        </template>
                        <div class="log-show log-show-datasync">
                            <component :is="currentTab" ref="containerInstanceRef" class="show-container" />
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
        </LoadingPage>
        <!-- 配置 -->
        <config-detail ref="configDetailRef"></config-detail>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted, markRaw, nextTick } from 'vue'
import Breadcrumb from '@/layout/bread-crumb/index.vue'
import LoadingPage from '@/components/loading/index.vue'
// import ConfigModal from './config-modal/index.vue'
import ConfigDetail from '../workflow-page/config-detail/index.vue'
import PublishLog from '../work-item/publish-log.vue'
import ReturnData from '../work-item/return-data.vue'
import RunningLog from '../work-item/running-log.vue'
import TotalDetail from '../work-item/total-detail.vue'

import { DeleteWorkData, GetWorkItemConfig, PublishWorkData, RunWorkItemConfig, SaveWorkItemConfig, TerWorkItemConfig } from '@/services/workflow.service'
import { GetFileCenterList } from '@/services/file-center.service'
import { ElMessage, ElMessageBox, ElInput, FormRules } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { Loading } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const emit = defineEmits(['back', 'locationNode'])

const props = defineProps<{
    workItemConfig: any,
    workFlowData: any
}>()

const loading = ref(false)
const networkError = ref(false)
const runningLoading = ref(false)
const saveLoading = ref(false)
const terLoading = ref(false)
const publishLoading = ref(false)
const stopLoading = ref(false)
const configDetailRef = ref()
const activeName = ref()
const currentTab = ref()
const instanceId = ref('')
const changeStatus = ref(false)
const fileIdList = ref([])

const containerInstanceRef = ref(null)

const logCollapseRef = ref()
const collapseActive = ref('0')
const isCollapse = ref(false)

let workConfig = reactive({
    workId: '',
    workType: ''
})
let jarJobConfig = reactive({
    appName: '',    // 应用名称
    jarFileId: '',  // 依赖
    mainClass: '',
    args: []        // 参数
})
const rules = reactive<FormRules>({
    appName: [{ required: true, message: '请输入应用名称', trigger: ['blur', 'change'] }],
    jarFileId: [{ required: true, message: '请选择资源文件', trigger: ['blur', 'change'] }],
    mainClass: [{ required: true, message: '请输入mainClass', trigger: ['blur', 'change'] }]
})

const tabList = reactive([
    {
        name: '提交日志',
        code: 'PublishLog',
        hide: false
    },
    {
        name: '运行日志',
        code: 'RunningLog',
        hide: true
    },
    // {
    //   name: '监控信息',
    //   code: 'TotalDetail',
    //   hide: true
    // }
])
function initData(id?: string, tableLoading?: boolean) {
    loading.value = tableLoading ? false : true
    networkError.value = networkError.value || false
    GetWorkItemConfig({
        workId: props.workItemConfig.id
    })
        .then((res: any) => {
            workConfig = res.data
            workConfig.workType = props.workItemConfig.workType
            if (res.data.jarJobConfig) {
                Object.keys(jarJobConfig).forEach((key: string) => {
                    jarJobConfig[key] = res.data.jarJobConfig[key]
                })
            }
            nextTick(() => {
                changeStatus.value = false
                containerInstanceRef.value.initData(id || instanceId.value, (status: string) => {
                    // 运行结束
                    if (workConfig.workType === 'FLINK_JAR' && id) {
                        tabList.forEach((item: any) => {
                            if (['RunningLog', 'TotalDetail'].includes(item.code)) {
                                item.hide = false
                            }
                            if (item.code === 'ReturnData') {
                                item.hide = status === 'FAIL' ? true : false
                            }
                        })
                    }
                })
            })
            loading.value = false
            networkError.value = false
        })
        .catch(() => {
            loading.value = false
            networkError.value = false
        })
}

function getFileCenterList() {
    GetFileCenterList({
        page: 0,
        pageSize: 10000,
        searchKeyWord: '',
        type: 'JOB'
    }).then((res: any) => {
        fileIdList.value = res.data.content.map(item => {
            return {
                label: item.fileName,
                value: item.id
            }
        })
    }).catch(() => {
        fileIdList.value = []
    })
}

function tabChangeEvent(e: string) {
    const lookup = {
        PublishLog: PublishLog,
        ReturnData: ReturnData,
        RunningLog: RunningLog,
        TotalDetail: TotalDetail
    }
    activeName.value = e
    currentTab.value = markRaw(lookup[e])
    nextTick(() => {
        containerInstanceRef.value.initData(instanceId.value)
    })
}

// 返回
function goBack() {
    if (changeStatus.value) {
        ElMessageBox.confirm('作业尚未保存，是否确定要返回吗？', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            emit('back', props.workItemConfig.id)
        })
    } else {
        emit('back', props.workItemConfig.id)
    }
}
function locationNode() {
    if (changeStatus.value) {
        ElMessageBox.confirm('作业尚未保存，是否确定要返回吗？', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            emit('locationNode', props.workItemConfig.id)
        })
    } else {
        emit('locationNode', props.workItemConfig.id)
    }
}

// 运行
function runWorkData() {
    if (changeStatus.value) {
        ElMessageBox.confirm('作业尚未保存，是否确定要运行作业？', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            tabList.forEach((item: any) => {
                if (['RunningLog', 'TotalDetail', 'ReturnData'].includes(item.code)) {
                    item.hide = true
                }
            })
            runningLoading.value = true
            // 点击运行，默认跳转到提交日志tab
            activeName.value = 'PublishLog'
            currentTab.value = markRaw(PublishLog)
            RunWorkItemConfig({
                workId: props.workItemConfig.id
            })
                .then((res: any) => {
                    runningLoading.value = false
                    instanceId.value = res.data.instanceId
                    ElMessage.success(res.msg)
                    initData(res.data.instanceId, true)
                    nextTick(() => {
                        changeCollapseUp()
                    })
                })
                .catch(() => {
                    runningLoading.value = false
                })
        })
    } else {
        tabList.forEach((item: any) => {
            if (['RunningLog', 'TotalDetail', 'ReturnData'].includes(item.code)) {
                item.hide = true
            }
        })
        runningLoading.value = true
        RunWorkItemConfig({
            workId: props.workItemConfig.id
        })
            .then((res: any) => {
                runningLoading.value = false
                instanceId.value = res.data.instanceId
                ElMessage.success(res.msg)
                initData(res.data.instanceId, true)

                // 点击运行，默认跳转到提交日志tab
                activeName.value = 'PublishLog'
                currentTab.value = markRaw(PublishLog)
                nextTick(() => {
                    changeCollapseUp()
                })
            })
            .catch(() => {
                runningLoading.value = false
            })
    }
}

// 终止
function terWorkData() {
    if (!instanceId.value) {
        ElMessage.warning('暂无可中止的作业')
        return
    }
    terLoading.value = true
    TerWorkItemConfig({
        workId: props.workItemConfig.id,
        instanceId: instanceId.value
    })
        .then((res: any) => {
            terLoading.value = false
            ElMessage.success(res.msg)
            initData('', true)
        })
        .catch(() => {
            terLoading.value = false
        })
}

// 保存配置
function saveData() {
    saveLoading.value = true
    SaveWorkItemConfig({
        workId: props.workItemConfig.id,
        jarJobConfig: jarJobConfig
    })
        .then((res: any) => {
            changeStatus.value = false
            ElMessage.success(res.msg)
            saveLoading.value = false
        })
        .catch(() => {
            saveLoading.value = false
        })
}

// 发布
function publishData() {
    publishLoading.value = true
    PublishWorkData({
        workId: props.workItemConfig.id
    }).then((res: any) => {
        ElMessage.success(res.msg)
        publishLoading.value = false
    })
        .catch((error: any) => {
            publishLoading.value = false
        })
}

// 下线
function stopData() {
    stopLoading.value = true
    DeleteWorkData({
        workId: props.workItemConfig.id
    }).then((res: any) => {
        ElMessage.success(res.msg)
        stopLoading.value = false
    })
        .catch((error: any) => {
            stopLoading.value = false
        })
}

// 配置打开
function setConfigData() {
    configDetailRef.value.showModal(props.workItemConfig)
}
function changeCollapseDown() {
  logCollapseRef.value.setActiveNames('0')
  isCollapse.value = false
}
function changeCollapseUp() {
  logCollapseRef.value.setActiveNames('1')
  isCollapse.value = true
}

function handleClose(index: number) {
    jarJobConfig.args.splice(index, 1)
}
function addParam(arr: string[]) {
    arr.push('')
}

onMounted(() => {
    initData()
    getFileCenterList()
    activeName.value = 'PublishLog'
    currentTab.value = markRaw(PublishLog)
})
</script>

<style lang="scss">
.zqy-flink-jar {
    position: relative;
    background-color: getCssVar('color', 'white');
    .zqy-loading {
        box-sizing: border-box;
        // overflow-y: auto;
        margin-top: 50px;
        padding: 0;
        // height: calc(100vh - 105px);

        .jar-work-container {
            overflow: auto;
            padding: 0 20px;
            height: calc(100vh - 108px);
            .sql-code-container {
                margin-top: 12px;
            }

            .work-item-log__collapse {
                position: absolute;
                left: 0;
                right: 0;
                bottom: 46px;
                z-index: 100;

                .el-collapse-item__header {
                    // padding-left: 20px;
                    cursor: default;
                }

                .el-collapse-item__arrow {
                    display: none;
                }

                .el-collapse-item__content {
                    padding-bottom: 14px;
                }

                .log__collapse {
                    position: absolute;
                    right: 20px;
                    cursor: pointer;
                }

                .el-tabs {
                    width: 100%;
                    // padding: 0 20px;
                    height: 40px;
                    box-sizing: border-box;

                    .el-tabs__item {
                        font-size: getCssVar('font-size', 'extra-small');
                    }

                    .el-tabs__nav-scroll {
                        padding-left: 20px;
                        box-sizing: border-box;
                    }

                    .el-tabs__content {
                        height: 0;
                    }

                    .el-tabs__nav-scroll {
                        border-bottom: 1px solid getCssVar('border-color');
                    }
                }

                .log-show {
                    padding: 0 20px;
                    box-sizing: border-box;

                    &.log-show-datasync {
                        height: calc(100vh - 306px);

                        .zqy-download-log {
                            right: 40px;
                            top: 12px;
                        }
                    }

                    pre {
                        width: 100px;
                    }

                    .show-container {
                        height: calc(100vh - 310px);
                        overflow: auto;
                    }

                    .empty-page {
                        height: 80%;
                    }
                }
            }
        }
    }
    .header-options {
        position: absolute;
        top: -50px;
        left: 0;
        padding-left: 18px;
        z-index: 500;

        display: flex;
        align-items: center;
        background-color: getCssVar('color', 'white');
        width: 100%;

        height: 50px;
        display: flex;
        align-items: center;
        color: getCssVar('color', 'primary', 'light-5');
        border-bottom: 1px solid getCssVar('border-color');

        .btn-box {
          font-size: getCssVar('font-size', 'extra-small');
          display: flex;
          cursor: pointer;
          width: 48px;
          margin-right: 8px;

          &.btn-box__4 {
            width: 70px;
          }

          .btn-text {
            margin-left: 4px;
          }

          &:hover {
            color: getCssVar('color', 'primary');;
          }
        }
    }
    .jar-args-container {
        margin-bottom: 0;
        .el-form-item {
            margin-bottom: 12px;
        }
        .el-form-item__label {
            margin-bottom: 0;
        }
        .el-form-item__content {
            position: relative;
            .el-scrollbar {
                width: 100%;
                .el-scrollbar__view {
                    max-height: calc(100vh - 450px);
                    padding-right: 20px;
                    box-sizing: border-box;
                }
            }
            .button-add {
                position: absolute;
                top: -27px;
                z-index: 10;
                left: 52px;
                color: getCssVar('color', 'primary');
                cursor: pointer;
                &:hover {
                    color: getCssVar('color', 'primary', 'light-3');
                }
            }
            .input-container {
                &+.input-container {
                    margin-top: 12px;
                }
                display: flex;
                width: 100%;
                align-items: center;
                .button-remove {
                    margin-left: 12px;
                    color: getCssVar('color', 'danger');
                    cursor: pointer;
                    &:hover {
                        color: getCssVar('color', 'danger', 'light-3');
                    }
                }
            }
        }
    }
}
</style>
