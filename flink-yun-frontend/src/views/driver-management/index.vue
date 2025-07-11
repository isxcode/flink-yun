<template>
  <Breadcrumb :bread-crumb-list="breadCrumbList" />
  <div class="zqy-seach-table driver-table">
    <div class="zqy-table-top">
      <el-button type="primary" @click="addData">
        添加驱动
      </el-button>
      <div class="zqy-seach">
        <el-input v-model="keyword" placeholder="请输入名称/备注 回车进行搜索" :maxlength="200" clearable @input="inputEvent"
          @keyup.enter="initData(false)" />
      </div>
    </div>
    <LoadingPage :visible="loading" :network-error="networkError" @loading-refresh="initData(false)">
      <div class="zqy-table">
        <BlockTable :table-config="tableConfig" @size-change="handleSizeChange" @current-change="handleCurrentChange">
          <template #defaultTag="scopeSlot">
            <div class="btn-group">
              <el-tag v-if="scopeSlot.row.isDefaultDriver" class="ml-2" type="success">
                是
              </el-tag>
              <el-tag v-if="!scopeSlot.row.isDefaultDriver" class="ml-2" type="danger">
                否
              </el-tag>
            </div>
          </template>
          <template #options="scopeSlot">
            <div class="btn-group">
              <span v-if="!scopeSlot.row.isDefaultDriver" @click="setDefaultDriver(scopeSlot.row)">默认</span>
              <span v-else @click="setDefaultDriver(scopeSlot.row)">取消</span>
               <el-dropdown trigger="click">
                <span class="click-show-more">更多</span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="editData(scopeSlot.row)">
                      备注
                    </el-dropdown-item>
                    <el-dropdown-item @click="deleteData(scopeSlot.row)">
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <!-- <el-icon v-else class="is-loading"><Loading /></el-icon> -->
            </div>
          </template>
        </BlockTable>
      </div>
    </LoadingPage>
    <AddModal ref="addModalRef" />
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import AddModal from './add-modal/index.vue'
// import { useState } from '@/hooks/useStore'
import Breadcrumb from '@/layout/bread-crumb/index.vue'
import BlockTable from '@/components/block-table/index.vue'
import LoadingPage from '@/components/loading/index.vue'

import { BreadCrumbList, TableConfig } from './driver.config'
import {
    GetDefaultDriverData,
    GetDriverListData,
    DeleteDefaultDriverData,
    AddDefaultDriverData,
    SetDefaultDriverData,
    UpdateDefaultDriverRemark
} from '@/services/driver-management.service'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/useAuth'

const router = useRouter()

const authStore = useAuthStore()
// const state = useState(['tenantId' ], 'authStoreModule')

const breadCrumbList = reactive(BreadCrumbList)
const tableConfig: any = reactive(TableConfig)
const keyword = ref('')
const loading = ref(false)
const networkError = ref(false)
const addModalRef = ref(null)

function initData(tableLoading?: boolean) {
  loading.value = tableLoading ? false : true
  networkError.value = networkError.value || false
  GetDriverListData({
    page: tableConfig.pagination.currentPage - 1,
    pageSize: tableConfig.pagination.pageSize,
    searchKeyWord: keyword.value
  })
    .then((res: any) => {
      tableConfig.tableData = res.data.content
      tableConfig.pagination.total = res.data.totalElements
      loading.value = false
      tableConfig.loading = false
      networkError.value = false
    })
    .catch(() => {
      tableConfig.tableData = []
      tableConfig.pagination.total = 0
      loading.value = false
      tableConfig.loading = false
      networkError.value = true
    })
}

function addData() {
  addModalRef.value.showModal((data: any) => {
    return new Promise((resolve: any, reject: any) => {
      const formData = new FormData()
      formData.append('dbType', data.dbType)
      formData.append('name', data.name)
      formData.append('remark', data.remark)
      formData.append('driver', data.driver)
      AddDefaultDriverData(formData)
        .then((res: any) => {
          ElMessage.success(res.data.msg)
          initData()
          resolve()
        })
        .catch((error: any) => {
          reject(error)
        })
    })
  })
}

// 删除
function deleteData(data: any) {
  ElMessageBox.confirm('确定删除该驱动吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    DeleteDefaultDriverData({
      driverId: data.id
    })
      .then((res: any) => {
        ElMessage.success(res.msg)
        initData()
      })
      .catch(() => { })
  })
}

// 修改备注
function editData(data: any) {
  addModalRef.value.showModal((formData: any) => {
    return new Promise((resolve: any, reject: any) => {
      UpdateDefaultDriverRemark({
        id: formData.id,
        remark: formData.remark
      }).then((res: any) => {
        ElMessage.success(res.msg)
        initData()
        resolve()
      }).catch((error: any) => {
        reject(error)
      })
    })
  }, data)
}

// 设置默认
function setDefaultDriver(data: any) {
  SetDefaultDriverData({
    driverId: data.id,
    isDefaultDriver: !data.isDefaultDriver
  }).then((res: any) => {
    ElMessage.success(res.msg)
    initData(true)
  }).catch(() => {
  })
}

function showDetail(data: any) {
  router.push({
    name: 'workflow-page',
    query: {
      id: data.id,
      name: data.name
    }
  })
}

function inputEvent(e: string) {
  if (e === '') {
    initData()
  }
}

function handleSizeChange(e: number) {
  tableConfig.pagination.pageSize = e
  initData()
}

function handleCurrentChange(e: number) {
  tableConfig.pagination.currentPage = e
  initData()
}

onMounted(() => {
  tableConfig.pagination.currentPage = 1
  tableConfig.pagination.pageSize = 10
  initData()
})
</script>

<style lang="scss">
.zqy-seach-table {
  .name-click {
    cursor: pointer;
    color: getCssVar('color', 'primary', 'light-5');

    &:hover {
      color: getCssVar('color', 'primary');
      ;
    }
  }
  &.driver-table {
    .zqy-table {
      .btn-group {
        // justify-content: center;
      }
    }
  }
}
</style>
