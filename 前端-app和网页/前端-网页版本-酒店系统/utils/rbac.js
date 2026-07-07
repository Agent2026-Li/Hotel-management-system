const MENU_ITEMS = [
	{ page: 'index', name: '仪表盘', icon: '📊' },
	{ page: 'room-status', name: '房态管理', icon: '🛏️' },
	{ page: 'reservation', name: '预订管理', icon: '📝' },
	{ page: 'checkin', name: '入住登记', icon: '🔑' },
	{ page: 'checkout', name: '退房结算', icon: '💳' },
	{ page: 'billing', name: '账单管理', icon: '📋' },
	{ page: 'housekeeping', name: '客房清洁', icon: '🧹' },
	{ page: 'shift', name: '交接班管理', icon: '🔁' },
	{ page: 'guest-history', name: '客户档案', icon: '👤' },
	{ page: 'reports', name: '报表统计', icon: '📈' },
	{ page: 'system', name: '系统设置', icon: '⚙️' }
]

const ROLE_MENUS = {
	ADMIN: ['index', 'room-status', 'reservation', 'checkin', 'checkout', 'billing', 'housekeeping', 'shift', 'guest-history', 'reports', 'system'],
	FRONT_DESK: ['index', 'room-status', 'reservation', 'checkin', 'checkout', 'shift', 'guest-history'],
	MANAGER: ['index', 'room-status', 'housekeeping', 'guest-history', 'reports'],
	HOUSEKEEPING: ['index', 'housekeeping'],
	FINANCE: ['index', 'checkout', 'billing', 'reports']
}

const ROLE_PERMISSIONS = {
	ADMIN: ['*'],
	FRONT_DESK: [
		'room:view',
		'room:change',
		'room:extend',
		'reservation:view',
		'reservation:create',
		'reservation:update',
		'reservation:cancel',
		'checkin:handle',
		'checkout:view',
		'checkout:handle',
		'guest:view',
		'guest:create',
		'guest:update_basic',
		'shift:create'
	],
	MANAGER: [
		'room:view',
		'room:update',
		'housekeeping:view',
		'housekeeping:assign',
		'housekeeping:update',
		'guest:view',
		'reports:view'
	],
	HOUSEKEEPING: [
		'housekeeping:view',
		'housekeeping:own',
		'housekeeping:update',
		'housekeeping:complete'
	],
	FINANCE: [
		'checkout:view',
		'checkout:handle',
		'billing:view',
		'billing:edit',
		'billing:refund',
		'payment:confirm',
		'reports:view',
		'reports:export',
		'revenue:view'
	]
}

const PAGE_NAMES = MENU_ITEMS.reduce((map, item) => {
	map[item.page] = item.name
	return map
}, {})

function normalizeRole(role) {
	return role || ''
}

function getAllowedPageKeys(role) {
	return ROLE_MENUS[normalizeRole(role)] || []
}

function getAllowedMenus(role) {
	const allowed = getAllowedPageKeys(role)
	return MENU_ITEMS.filter(item => allowed.includes(item.page))
}

function getPageName(page) {
	return PAGE_NAMES[page] || page
}

function can(permission, role) {
	const permissions = ROLE_PERMISSIONS[normalizeRole(role)] || []
	return permissions.includes('*') || permissions.includes(permission)
}

function canAny(permissions, role) {
	return permissions.some(permission => can(permission, role))
}

function canAccessPage(page, role) {
	if (page === 'login') {
		return true
	}
	return getAllowedPageKeys(role).includes(page)
}

function firstAllowedPage(role) {
	const pages = getAllowedPageKeys(role)
	return pages.length ? pages[0] : 'index'
}

function pageFromRoute(route) {
	if (!route) {
		return ''
	}
	const match = route.match(/^pages\/([^/]+)\/index$/)
	return match ? match[1] : route
}

function pageUrl(page, query) {
	let url = `/pages/${page}/index`
	const params = Object.keys(query || {})
		.filter(key => query[key] !== undefined && query[key] !== null && query[key] !== '')
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(query[key])}`)
	if (params.length) {
		url += `?${params.join('&')}`
	}
	return url
}

export default {
	MENU_ITEMS,
	ROLE_MENUS,
	ROLE_PERMISSIONS,
	getAllowedMenus,
	getPageName,
	can,
	canAny,
	canAccessPage,
	firstAllowedPage,
	pageFromRoute,
	pageUrl
}
