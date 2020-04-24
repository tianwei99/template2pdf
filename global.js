import request from '@/plugin/axios'
// 导出公共方法
export function exportData (url, params, fileName) {
  let supInfo = localStorage.getItem('supInfo')
  if (!supInfo) {
    supInfo = {}
  } else {
    supInfo = JSON.parse(supInfo)
  }
  params.supCode = supInfo.supCode
  params.token = localStorage.getItem('token')
  let newData = getNowFormatDateFun()
  fileName = fileName + newData
  let suffix = '.xlsx'
  axiosFile(url, params, r => {
    const content = r
    let responseType = r.type
    const blob = new Blob([content], {
      type: responseType
    })
    if (r.type === 'text/xml') {
      suffix = '.xls'
    } else if (r.type === 'application/pdf') {
      suffix = '.pdf'
    }
    if ((r.type !== 'application/pdf') && (r.type !== 'application/vnd.ms-excel' && r.type !== 'text/xml' && r.type !== 'application/octet-stream')) {
      let reader = new FileReader()
      reader.readAsText(blob, 'utf-8')
      let _this = this
      reader.onload = function (e) {
        let resultJson = JSON.parse(reader.result)
        _this.$message.error(resultJson.msg)
      }
      return
    }
    fileName = fileName + suffix
    if ('download' in document.createElement('a')) {
      // 非IE下载
      const elink = document.createElement('a')
      elink.download = fileName
      elink.style.display = 'none'
      elink.href = URL.createObjectURL(blob)
      document.body.appendChild(elink)
      elink.click()
      URL.revokeObjectURL(elink.href) // 释放URL 对象
      document.body.removeChild(elink)
    } else {
      // IE10+下载
      navigator.msSaveBlob(blob, fileName)
    }
  })
}
// POST输出文件流
function axiosFile (url, data, success, failure) {
  request({
    method: 'post',
    url: url,
    data: data,
    responseType: 'blob'
  })
    .then(function (res) {
      success(res)
    })
}

function getNowFormatDateFun () {
  let date = new Date()
  let seperator1 = ''
  let seperator2 = ''
  let month = date.getMonth() + 1
  let strDate = date.getDate()
  let minutes = date.getMinutes()
  if (month >= 1 && month <= 9) {
    month = '0' + month
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = '0' + strDate
  }
  if (minutes >= 0 && minutes <= 9) {
    minutes = '0' + minutes
  }
  let currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate +
      '' + date.getHours() + seperator2 + minutes +
      seperator2 + date.getSeconds()
  return currentdate
}
