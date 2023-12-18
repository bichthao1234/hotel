export class StringUtil {
  static formatString(str: string, params: any) {
    Object.keys(params).forEach(key => {
      str = str.replace(new RegExp('\\{' + key + '\\}', 'gi'), params[key]);
    });
    return str;
  }
}
