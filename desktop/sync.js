const fs = require('fs');
const path = require('path');

// 单一源码：根目录 index.html 同步到桌面壳内再打包
fs.copyFileSync(path.join(__dirname, '..', 'index.html'), path.join(__dirname, 'index.html'));
console.log('[polyforge] index.html synced for packaging');
