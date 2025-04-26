/**
 * Date utility functions
 */

/**
 * Format date to string
 * @param {Date|String} date - Date to format
 * @param {String} fmt - Format string (yyyy-MM-dd HH:mm:ss)
 * @returns {String} Formatted date string
 */
export function formatDate(date, fmt = 'yyyy-MM-dd HH:mm:ss') {
  if (!date) {
    return ''
  }
  
  if (typeof date === 'string') {
    date = new Date(date.replace(/-/g, '/'))
  }
  
  if (!(date instanceof Date) || isNaN(date.getTime())) {
    return ''
  }
  
  const o = {
    'M+': date.getMonth() + 1, // month
    'd+': date.getDate(), // day
    'H+': date.getHours(), // hour
    'm+': date.getMinutes(), // minute
    's+': date.getSeconds(), // second
    'q+': Math.floor((date.getMonth() + 3) / 3), // quarter
    'S': date.getMilliseconds() // millisecond
  }
  
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  
  return fmt
}

/**
 * Get relative time string
 * @param {Date|String} date - Date to format
 * @returns {String} Relative time string (e.g. "2 hours ago", "in 3 days")
 */
export function getRelativeTime(date) {
  if (!date) {
    return ''
  }
  
  if (typeof date === 'string') {
    date = new Date(date.replace(/-/g, '/'))
  }
  
  if (!(date instanceof Date) || isNaN(date.getTime())) {
    return ''
  }
  
  const now = new Date()
  const diff = Math.floor((now.getTime() - date.getTime()) / 1000)
  
  if (diff < 0) {
    // Future date
    const seconds = Math.abs(diff)
    if (seconds < 60) {
      return `in ${seconds} second${seconds === 1 ? '' : 's'}`
    }
    
    const minutes = Math.floor(seconds / 60)
    if (minutes < 60) {
      return `in ${minutes} minute${minutes === 1 ? '' : 's'}`
    }
    
    const hours = Math.floor(minutes / 60)
    if (hours < 24) {
      return `in ${hours} hour${hours === 1 ? '' : 's'}`
    }
    
    const days = Math.floor(hours / 24)
    if (days < 30) {
      return `in ${days} day${days === 1 ? '' : 's'}`
    }
    
    const months = Math.floor(days / 30)
    if (months < 12) {
      return `in ${months} month${months === 1 ? '' : 's'}`
    }
    
    const years = Math.floor(months / 12)
    return `in ${years} year${years === 1 ? '' : 's'}`
  } else {
    // Past date
    if (diff < 60) {
      return `${diff} second${diff === 1 ? '' : 's'} ago`
    }
    
    const minutes = Math.floor(diff / 60)
    if (minutes < 60) {
      return `${minutes} minute${minutes === 1 ? '' : 's'} ago`
    }
    
    const hours = Math.floor(minutes / 60)
    if (hours < 24) {
      return `${hours} hour${hours === 1 ? '' : 's'} ago`
    }
    
    const days = Math.floor(hours / 24)
    if (days < 30) {
      return `${days} day${days === 1 ? '' : 's'} ago`
    }
    
    const months = Math.floor(days / 30)
    if (months < 12) {
      return `${months} month${months === 1 ? '' : 's'} ago`
    }
    
    const years = Math.floor(months / 12)
    return `${years} year${years === 1 ? '' : 's'} ago`
  }
} 