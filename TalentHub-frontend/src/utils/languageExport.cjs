const { google } = require('googleapis');
const fs = require('fs');
const path = require('path');
require('dotenv').config();


async function getAuthClient() {
    const auth = new google.auth.GoogleAuth({
        credentials: {
            type: process.env.GOOGLE_TYPE,
            project_id: process.env.GOOGLE_PROJECT_ID,
            private_key_id: process.env.GOOGLE_PRIVATE_KEY_ID,
            private_key: process.env.GOOGLE_PRIVATE_KEY?.replace(/\\n/g, '\n'),
            client_email: process.env.GOOGLE_CLIENT_EMAIL,
            client_id: process.env.GOOGLE_CLIENT_ID,
            auth_uri: process.env.GOOGLE_AUTH_URI,
            token_uri: process.env.GOOGLE_TOKEN_URI,
            auth_provider_x509_cert_url: process.env.GOOGLE_AUTH_PROVIDER_X509_CERT_URL,
            client_x509_cert_url: process.env.GOOGLE_CLIENT_X509_CERT_URL,
            universe_domain: process.env.GOOGLE_UNIVERSE_DOMAIN
        },
        scopes: ['https://www.googleapis.com/auth/spreadsheets.readonly']
    });
    return auth.getClient();
}

async function validateRow(row, rowIndex) {
    const [key, vi, en] = row;
    if (!key || !vi || !en) {
        console.warn('\n🚨 CẢNH BÁO 🚨');
        console.warn('┌─────────────────────────────────────────┐');
        console.warn(`│ Dòng ${rowIndex + 1} thiếu dữ liệu! Bỏ qua...      │`);
        console.warn('└─────────────────────────────────────────┘');
        return false;
    }
    return true;
}


async function exportLanguageFiles() {
    // Dynamic imports for ESM modules
    const [chalk, gradient] = await Promise.all([
        import('chalk').then(m => m.default),
        import('gradient-string').then(m => m.default)
    ]);

    try {
        // Tạo signature với gradient màu
        const signature = gradient(['#FF512F', '#DD2476', '#FF512F']).multiline([
            '  ╔═══════════════════════════════════════╗',
            '  ║           Language Exporter           ║',
            '  ║          © 2025 Quang Bùi             ║',
            '  ╚═══════════════════════════════════════╝'
        ].join('\n'));

        console.log('\n' + signature + '\n');

        console.log(chalk.bgCyan.white.bold(' KHỞI ĐỘNG ') + chalk.cyan(' 🚀 Bắt đầu xuất file ngôn ngữ...\n'));

        const authClient = await getAuthClient();
        const sheets = google.sheets({ version: 'v4', auth: authClient });

        console.log(chalk.blue('📊 ') + chalk.blue.bold('Đang kết nối với Google Sheets...'));
        const spreadsheetId = '1N674_gWnhRI1qXfgDooNaO-d2omMfGP6DS8lJmRhNCc';
        const response = await sheets.spreadsheets.values.get({
            spreadsheetId,
            range: 'Sheet1',
        });

        const rows = response.data.values;
        if (!rows?.length) {
            throw new Error('❌ Không tìm thấy dữ liệu trong bảng tính!');
        }

        console.log(chalk.magenta('📝 ') + chalk.magenta.bold('Đang xử lý dữ liệu...'));
        const data = rows.slice(1);
        const languages = {
            vi: {},
            en: {},
            ko: {},
            ja: {},
            zhcn: {},
        };

        // Generate type interface based on keys
        const keys = data.map(row => row[0]).filter(Boolean);
        const typeInterface = `export interface TranslationType {
${keys.map(key => `  "${key}": string;`).join('\n')}
}\n\n`;

        // Process each row
        data.forEach((row, index) => {
            if (validateRow(row, index)) {
                const [key, vi, en, ko, ja, zhcn] = row;
                languages.vi[key] = vi.trim();
                languages.en[key] = en.trim();
                languages.ko[key] = ko.trim();
                languages.ja[key] = ja.trim();
                languages.zhcn[key] = zhcn.trim();
            }
        });

        console.log('\n' + chalk.yellow('📂 ') + chalk.yellow.bold('Đang tạo thư mục locales...'));
        const localesDir = path.resolve(process.cwd(), 'src/locales');
        if (!fs.existsSync(localesDir)) {
            fs.mkdirSync(localesDir, { recursive: true });
        }

        const typesPath = path.join(localesDir, 'types.ts');
        fs.writeFileSync(typesPath, typeInterface);
        console.log('\n' + chalk.green('✨ ') + chalk.green.bold('Thành công: ') + chalk.green('Đã tạo file types.ts'));
        console.log(chalk.green('├─ 📝 Chứa các định nghĩa kiểu dữ liệu'));
        console.log(chalk.green('└─ 🔍 Hỗ trợ TypeScript type checking\n'));

        console.log(chalk.cyan('🌍 ') + chalk.cyan.bold('Đang xuất các file ngôn ngữ:'));
        for (const lang of Object.keys(languages)) {
            const content = `import { TranslationType } from './types';\n\nexport const ${lang}: TranslationType = ${JSON.stringify(languages[lang], null, 2)};\n`;
            const filePath = path.join(localesDir, `${lang}.ts`);
            fs.writeFileSync(filePath, content);
            console.log(chalk.cyan(`├─ 🎯 ${lang.toUpperCase()}: `) + chalk.greenBright.bold('Xuất thành công!'));
        }

        const rainbow = gradient(['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#8F00FF']);
        console.log('\n' + rainbow('🎉 HOÀN THÀNH 🎉'));
        console.log(chalk.greenBright('┌────────────────────────────────────────┐'));
        console.log(chalk.greenBright('│  ✅ ') + chalk.white.bold('Đã xuất toàn bộ file ngôn ngữ  ') + chalk.greenBright('│'));
        console.log(chalk.greenBright('│  ✅ ') + chalk.white.bold('Kiểm tra thư mục src/locales    ') + chalk.greenBright('│'));
        console.log(chalk.greenBright('└────────────────────────────────────────┘\n'));

        console.log(gradient(['#00FF00', '#00FFFF']).multiline([
            '  =*==*==*==⭐️ Powered by Quang Bùi ⭐️==*==*==*=',
        ].join('\n')) + '\n');

    } catch (error) {
        console.error('\n' + chalk.red.bold('❌ LỖI ❌'));
        console.error(chalk.red('┌─────────────────────────────────────┐'));
        console.error(chalk.red(`│ ${error.message.padEnd(35)} │`));
        console.error(chalk.red('└─────────────────────────────────────┘\n'));
        throw error;
    }
}

module.exports = {
    exportLanguageFiles
};